package com.ridelink.ride.service;

import com.ridelink.ride.dto.AssignDriverRequest;
import com.ridelink.ride.dto.CreateRideRequest;
import com.ridelink.ride.dto.RideResponse;

public interface RideService {

    RideResponse createRide(CreateRideRequest request);

    RideResponse getRideById(String id);

    RideResponse assignDriver(String rideId, AssignDriverRequest request);
}
