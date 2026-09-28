package com.ridelink.driver.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.ridelink.driver.document.Vehicle;

@Repository
public interface VehicleRepository extends MongoRepository<Vehicle, String> {
}
