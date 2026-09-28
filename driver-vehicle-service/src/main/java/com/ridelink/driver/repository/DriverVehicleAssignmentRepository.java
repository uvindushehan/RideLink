package com.ridelink.driver.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.ridelink.driver.document.DriverVehicleAssignment;

@Repository
public interface DriverVehicleAssignmentRepository extends MongoRepository<DriverVehicleAssignment, String> {
    List<DriverVehicleAssignment> findByDriverId(String driverId);
}

