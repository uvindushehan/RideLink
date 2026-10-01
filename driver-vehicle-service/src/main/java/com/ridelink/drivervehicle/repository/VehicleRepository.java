package com.ridelink.drivervehicle.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.ridelink.drivervehicle.document.Vehicle;

@Repository
public interface VehicleRepository extends MongoRepository<Vehicle, String> {
}
