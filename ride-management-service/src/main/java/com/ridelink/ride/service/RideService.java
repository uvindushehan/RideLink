package com.ridelink.ride.service;

import com.ridelink.ride.dto.external.AvailableDriverResponse;
import com.ridelink.ride.dto.AssignDriverRequest;
import com.ridelink.ride.dto.CreateRideRequest;
import com.ridelink.ride.dto.RideResponse;

import java.util.List;

public interface RideService {

    RideResponse createRide(CreateRideRequest request);

    RideResponse getRideById(String id);

    RideResponse assignDriver(String rideId, AssignDriverRequest request);

    List<AvailableDriverResponse> getAvailableDrivers();

    RideResponse autoAssignDriver(String rideId);

    RideResponse acceptRide(String rideId);
}
