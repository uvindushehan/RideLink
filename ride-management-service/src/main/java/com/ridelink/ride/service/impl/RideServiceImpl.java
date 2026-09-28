package com.ridelink.ride.service.impl;

import com.ridelink.ride.document.Location;
import com.ridelink.ride.document.Ride;
import com.ridelink.ride.dto.CreateRideRequest;
import com.ridelink.ride.dto.LocationDto;
import com.ridelink.ride.dto.RideResponse;
import com.ridelink.ride.enums.RideStatus;
import com.ridelink.ride.repository.RideRepository;
import com.ridelink.ride.service.RideService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class RideServiceImpl implements RideService {

    private final RideRepository rideRepository;

    public RideServiceImpl(RideRepository rideRepository) {
        this.rideRepository = rideRepository;
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
