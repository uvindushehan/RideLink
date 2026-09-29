package com.ridelink.drivervehicle.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.ridelink.drivervehicle.document.Driver;

public interface DriverRepository extends MongoRepository<Driver, String> {
}
