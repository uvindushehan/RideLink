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
