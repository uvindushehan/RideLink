package com.ridelink.ride.service.impl;

import com.ridelink.ride.client.DriverServiceClient;
import com.ridelink.ride.document.Location;
import com.ridelink.ride.document.Ride;
import com.ridelink.ride.dto.AssignDriverRequest;
import com.ridelink.ride.dto.CreateRideRequest;
import com.ridelink.ride.dto.LocationDto;
import com.ridelink.ride.dto.RideResponse;
import com.ridelink.ride.dto.external.AvailableDriverResponse;
import com.ridelink.ride.enums.RideStatus;
import com.ridelink.ride.exception.DriverServiceUnavailableException;
import com.ridelink.ride.exception.InvalidRideStatusException;
import com.ridelink.ride.exception.NoAvailableDriverException;
import com.ridelink.ride.exception.RideNotFoundException;
import com.ridelink.ride.repository.RideRepository;
import com.ridelink.ride.validation.RideStatusValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RideServiceImplTest {

    @Mock
    private RideRepository rideRepository;

    @Mock
    private DriverServiceClient driverServiceClient;

    @Spy
    private RideStatusValidator rideStatusValidator;

    @InjectMocks
    private RideServiceImpl rideService;

    private Ride testRide;

    @BeforeEach
    void setUp() {
        testRide = new Ride();
        testRide.setId("ride-123");
        testRide.setPassengerId("passenger-123");
        testRide.setStatus(RideStatus.REQUESTED);
    }

    @Test
    void createRide_shouldCreateRequestedRide() {
        CreateRideRequest request = new CreateRideRequest();
        request.setPassengerId("passenger-123");
        request.setPickupLocation(new LocationDto(1.0, 1.0, "Pickup"));
        request.setDestinationLocation(new LocationDto(2.0, 2.0, "Dropoff"));

        when(rideRepository.save(any(Ride.class))).thenAnswer(invocation -> {
            Ride r = invocation.getArgument(0);
            r.setId("ride-123");
            return r;
        });

        RideResponse response = rideService.createRide(request);

        assertNotNull(response);
        assertEquals("passenger-123", response.getPassengerId());
        assertNull(response.getDriverId());
        assertEquals(RideStatus.REQUESTED, response.getStatus());
        verify(rideRepository).save(any(Ride.class));
    }

    @Test
    void getRideById_shouldReturnRideWhenFound() {
        when(rideRepository.findById("ride-123")).thenReturn(Optional.of(testRide));

        RideResponse response = rideService.getRideById("ride-123");

        assertNotNull(response);
        assertEquals("ride-123", response.getId());
        assertEquals(RideStatus.REQUESTED, response.getStatus());
    }

    @Test
    void getRideById_shouldThrowRideNotFoundWhenMissing() {
        when(rideRepository.findById("invalid-id")).thenReturn(Optional.empty());

        assertThrows(RideNotFoundException.class, () -> rideService.getRideById("invalid-id"));
    }

    @Test
    void assignDriver_shouldAssignDriverToRequestedRide() {
        when(rideRepository.findById("ride-123")).thenReturn(Optional.of(testRide));
        when(rideRepository.save(any(Ride.class))).thenReturn(testRide);
        
        AssignDriverRequest request = new AssignDriverRequest("driver-123");
        
        RideResponse response = rideService.assignDriver("ride-123", request);
        
        assertEquals(RideStatus.ASSIGNED, response.getStatus());
        assertEquals("driver-123", response.getDriverId());
        verify(rideRepository).save(testRide);
    }

    @Test
    void assignDriver_shouldRejectInvalidRideStatus() {
        testRide.setStatus(RideStatus.IN_PROGRESS);
        when(rideRepository.findById("ride-123")).thenReturn(Optional.of(testRide));
        
        AssignDriverRequest request = new AssignDriverRequest("driver-123");
        
        assertThrows(InvalidRideStatusException.class, () -> rideService.assignDriver("ride-123", request));
    }

    @Test
    void autoAssignDriver_shouldSelectFirstAvailableDriver() {
        when(rideRepository.findById("ride-123")).thenReturn(Optional.of(testRide));
        when(driverServiceClient.getAvailableDrivers()).thenReturn(List.of(
                new AvailableDriverResponse("driver-1", "AVAILABLE"),
                new AvailableDriverResponse("driver-2", "AVAILABLE")
        ));
        when(rideRepository.save(any(Ride.class))).thenReturn(testRide);

        RideResponse response = rideService.autoAssignDriver("ride-123");

        assertEquals(RideStatus.ASSIGNED, response.getStatus());
        assertEquals("driver-1", response.getDriverId());
        verify(rideRepository).save(testRide);
    }

    @Test
    void autoAssignDriver_shouldThrowWhenNoDriversAvailable() {
        when(rideRepository.findById("ride-123")).thenReturn(Optional.of(testRide));
        when(driverServiceClient.getAvailableDrivers()).thenReturn(Collections.emptyList());

        assertThrows(NoAvailableDriverException.class, () -> rideService.autoAssignDriver("ride-123"));
    }

    @Test
    void autoAssignDriver_shouldPropagateDriverServiceUnavailable() {
        when(rideRepository.findById("ride-123")).thenReturn(Optional.of(testRide));
        when(driverServiceClient.getAvailableDrivers()).thenThrow(new DriverServiceUnavailableException("Service down"));

        assertThrows(DriverServiceUnavailableException.class, () -> rideService.autoAssignDriver("ride-123"));
    }

    @Test
    void acceptRide_shouldChangeAssignedToAccepted() {
        testRide.setStatus(RideStatus.ASSIGNED);
        testRide.setDriverId("driver-123");
        when(rideRepository.findById("ride-123")).thenReturn(Optional.of(testRide));
        when(rideRepository.save(any(Ride.class))).thenReturn(testRide);

        RideResponse response = rideService.acceptRide("ride-123");

        assertEquals(RideStatus.ACCEPTED, response.getStatus());
        verify(rideRepository).save(testRide);
    }

    @Test
    void acceptRide_shouldRejectInvalidStatus() {
        when(rideRepository.findById("ride-123")).thenReturn(Optional.of(testRide)); // Status is REQUESTED

        assertThrows(InvalidRideStatusException.class, () -> rideService.acceptRide("ride-123"));
    }

    @Test
    void startRide_shouldChangeAcceptedToInProgress() {
        testRide.setStatus(RideStatus.ACCEPTED);
        testRide.setDriverId("driver-123");
        when(rideRepository.findById("ride-123")).thenReturn(Optional.of(testRide));
        when(rideRepository.save(any(Ride.class))).thenReturn(testRide);

        RideResponse response = rideService.startRide("ride-123");

        assertEquals(RideStatus.IN_PROGRESS, response.getStatus());
        verify(rideRepository).save(testRide);
    }

    @Test
    void startRide_shouldRejectInvalidStatus() {
        when(rideRepository.findById("ride-123")).thenReturn(Optional.of(testRide)); // Status is REQUESTED

        assertThrows(InvalidRideStatusException.class, () -> rideService.startRide("ride-123"));
    }

    @Test
    void completeRide_shouldChangeInProgressToCompleted() {
        testRide.setStatus(RideStatus.IN_PROGRESS);
        when(rideRepository.findById("ride-123")).thenReturn(Optional.of(testRide));
        when(rideRepository.save(any(Ride.class))).thenReturn(testRide);

        RideResponse response = rideService.completeRide("ride-123");

        assertEquals(RideStatus.COMPLETED, response.getStatus());
        verify(rideRepository).save(testRide);
    }

    @Test
    void completeRide_shouldRejectInvalidStatus() {
        when(rideRepository.findById("ride-123")).thenReturn(Optional.of(testRide)); // Status is REQUESTED

        assertThrows(InvalidRideStatusException.class, () -> rideService.completeRide("ride-123"));
    }

    @Test
    void cancelRide_shouldCancelFromRequested() {
        when(rideRepository.findById("ride-123")).thenReturn(Optional.of(testRide));
        when(rideRepository.save(any(Ride.class))).thenReturn(testRide);

        RideResponse response = rideService.cancelRide("ride-123");

        assertEquals(RideStatus.CANCELLED, response.getStatus());
        verify(rideRepository).save(testRide);
    }

    @Test
    void cancelRide_shouldCancelFromAssigned() {
        testRide.setStatus(RideStatus.ASSIGNED);
        when(rideRepository.findById("ride-123")).thenReturn(Optional.of(testRide));
        when(rideRepository.save(any(Ride.class))).thenReturn(testRide);

        RideResponse response = rideService.cancelRide("ride-123");

        assertEquals(RideStatus.CANCELLED, response.getStatus());
    }

    @Test
    void cancelRide_shouldCancelFromAccepted() {
        testRide.setStatus(RideStatus.ACCEPTED);
        when(rideRepository.findById("ride-123")).thenReturn(Optional.of(testRide));
        when(rideRepository.save(any(Ride.class))).thenReturn(testRide);

        RideResponse response = rideService.cancelRide("ride-123");

        assertEquals(RideStatus.CANCELLED, response.getStatus());
    }

    @Test
    void cancelRide_shouldRejectCancelFromInProgress() {
        testRide.setStatus(RideStatus.IN_PROGRESS);
        when(rideRepository.findById("ride-123")).thenReturn(Optional.of(testRide));

        assertThrows(InvalidRideStatusException.class, () -> rideService.cancelRide("ride-123"));
    }

    @Test
    void cancelRide_shouldRejectCancelFromCompleted() {
        testRide.setStatus(RideStatus.COMPLETED);
        when(rideRepository.findById("ride-123")).thenReturn(Optional.of(testRide));

        assertThrows(InvalidRideStatusException.class, () -> rideService.cancelRide("ride-123"));
    }

    @Test
    void cancelRide_shouldRejectCancelFromCancelled() {
        testRide.setStatus(RideStatus.CANCELLED);
        when(rideRepository.findById("ride-123")).thenReturn(Optional.of(testRide));

        assertThrows(InvalidRideStatusException.class, () -> rideService.cancelRide("ride-123"));
    }
}
