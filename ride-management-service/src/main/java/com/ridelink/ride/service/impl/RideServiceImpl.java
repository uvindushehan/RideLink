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
import com.ridelink.ride.exception.InvalidRideStatusException;
import com.ridelink.ride.exception.NoAvailableDriverException;
import com.ridelink.ride.exception.RideNotFoundException;
import com.ridelink.ride.repository.RideRepository;
import com.ridelink.ride.service.RideService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RideServiceImpl implements RideService {

    private final RideRepository rideRepository;
    private final DriverServiceClient driverServiceClient;

    public RideServiceImpl(RideRepository rideRepository, DriverServiceClient driverServiceClient) {
        this.rideRepository = rideRepository;
        this.driverServiceClient = driverServiceClient;
    }

    @Override
    public RideResponse createRide(CreateRideRequest request) {
        Ride ride = new Ride();
        ride.setPassengerId(request.getPassengerId());
        ride.setPickupLocation(mapToLocation(request.getPickupLocation()));
        ride.setDestinationLocation(mapToLocation(request.getDestinationLocation()));
        ride.setDriverId(null);
        ride.setStatus(RideStatus.REQUESTED);
        
        LocalDateTime now = LocalDateTime.now();
        ride.setCreatedAt(now);
        ride.setUpdatedAt(now);

        Ride savedRide = rideRepository.save(ride);

        return mapToRideResponse(savedRide);
    }

    @Override
    public RideResponse getRideById(String id) {
        Ride ride = rideRepository.findById(id)
                .orElseThrow(() -> new RideNotFoundException("Ride with ID " + id + " was not found"));
        return mapToRideResponse(ride);
    }

    @Override
    public RideResponse assignDriver(String rideId, AssignDriverRequest request) {
        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new RideNotFoundException("Ride with ID " + rideId + " was not found"));

        if (ride.getStatus() != RideStatus.REQUESTED) {
            throw new InvalidRideStatusException("Driver can only be assigned to a ride in REQUESTED status");
        }

        ride.setDriverId(request.getDriverId());
        ride.setStatus(RideStatus.ASSIGNED);
        ride.setUpdatedAt(LocalDateTime.now());

        Ride savedRide = rideRepository.save(ride);
        return mapToRideResponse(savedRide);
    }

    @Override
    public List<AvailableDriverResponse> getAvailableDrivers() {
        return driverServiceClient.getAvailableDrivers();
    }

    @Override
    public RideResponse autoAssignDriver(String rideId) {
        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new RideNotFoundException("Ride with ID " + rideId + " was not found"));

        if (ride.getStatus() != RideStatus.REQUESTED) {
            throw new InvalidRideStatusException("Driver can only be auto-assigned to a ride in REQUESTED status");
        }

        List<AvailableDriverResponse> availableDrivers = driverServiceClient.getAvailableDrivers();
        if (availableDrivers == null || availableDrivers.isEmpty()) {
            throw new NoAvailableDriverException("No available driver was found for this ride");
        }

        String selectedDriverId = availableDrivers.get(0).getDriverId();

        ride.setDriverId(selectedDriverId);
        ride.setStatus(RideStatus.ASSIGNED);
        ride.setUpdatedAt(LocalDateTime.now());

        Ride savedRide = rideRepository.save(ride);
        return mapToRideResponse(savedRide);
    }

    @Override
    public RideResponse acceptRide(String rideId) {
        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new RideNotFoundException("Ride with ID " + rideId + " was not found"));

        if (ride.getStatus() != RideStatus.ASSIGNED) {
            throw new InvalidRideStatusException("Only ASSIGNED rides can be accepted");
        }

        if (ride.getDriverId() == null || ride.getDriverId().trim().isEmpty()) {
            throw new InvalidRideStatusException("Ride cannot be accepted without an assigned driver");
        }

        ride.setStatus(RideStatus.ACCEPTED);
        ride.setUpdatedAt(LocalDateTime.now());

        Ride savedRide = rideRepository.save(ride);
        return mapToRideResponse(savedRide);
    }

    @Override
    public RideResponse startRide(String rideId) {
        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new RideNotFoundException("Ride with ID " + rideId + " was not found"));

        if (ride.getStatus() != RideStatus.ACCEPTED) {
            throw new InvalidRideStatusException("Only ACCEPTED rides can be started");
        }

        if (ride.getDriverId() == null || ride.getDriverId().trim().isEmpty()) {
            throw new InvalidRideStatusException("Ride cannot start without an assigned driver");
        }

        ride.setStatus(RideStatus.IN_PROGRESS);
        ride.setUpdatedAt(LocalDateTime.now());

        Ride savedRide = rideRepository.save(ride);
        return mapToRideResponse(savedRide);
    }

    @Override
    public RideResponse cancelRide(String rideId) {
        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new RideNotFoundException("Ride with ID " + rideId + " was not found"));

        RideStatus currentStatus = ride.getStatus();
        boolean cancellable = currentStatus == RideStatus.REQUESTED
                || currentStatus == RideStatus.ASSIGNED
                || currentStatus == RideStatus.ACCEPTED;

        if (!cancellable) {
            throw new InvalidRideStatusException(
                    "Ride cannot be cancelled in its current status: " + currentStatus);
        }

        ride.setStatus(RideStatus.CANCELLED);
        ride.setUpdatedAt(LocalDateTime.now());

        Ride savedRide = rideRepository.save(ride);
        return mapToRideResponse(savedRide);
    }

    private Location mapToLocation(LocationDto dto) {
        if (dto == null) {
            return null;
        }
        return new Location(dto.getLatitude(), dto.getLongitude(), dto.getPlaceName());
    }

    private LocationDto mapToLocationDto(Location location) {
        if (location == null) {
            return null;
        }
        return new LocationDto(location.getLatitude(), location.getLongitude(), location.getPlaceName());
    }

    private RideResponse mapToRideResponse(Ride ride) {
        if (ride == null) {
            return null;
        }
        RideResponse response = new RideResponse();
        response.setId(ride.getId());
        response.setPassengerId(ride.getPassengerId());
        response.setDriverId(ride.getDriverId());
        response.setPickupLocation(mapToLocationDto(ride.getPickupLocation()));
        response.setDestinationLocation(mapToLocationDto(ride.getDestinationLocation()));
        response.setStatus(ride.getStatus());
        response.setCreatedAt(ride.getCreatedAt());
        response.setUpdatedAt(ride.getUpdatedAt());
        return response;
    }
}
