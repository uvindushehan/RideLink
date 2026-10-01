package com.ridelink.drivervehicle.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.ridelink.drivervehicle.document.DriverVehicleAssignment;

@Repository
public interface DriverVehicleAssignmentRepository extends MongoRepository<DriverVehicleAssignment, String> {
    List<DriverVehicleAssignment> findByDriverId(String driverId);
    boolean existsByDriverIdAndActive(String driverId, boolean active);
    boolean existsByVehicleIdAndActive(String vehicleId, boolean active);
}


