package com.ridelink.drivervehicle.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ridelink.drivervehicle.document.Driver;
import com.ridelink.drivervehicle.enums.DriverAvailability;

public interface DriverRepository extends MongoRepository<Driver, String> {
    List<Driver> findByAvailabilityStatus(DriverAvailability availabilityStatus);
}
