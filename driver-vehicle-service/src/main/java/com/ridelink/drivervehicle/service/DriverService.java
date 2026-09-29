package com.ridelink.drivervehicle.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ridelink.drivervehicle.document.Driver;
import com.ridelink.drivervehicle.repository.DriverRepository;

@Service
public class DriverService {

    private final DriverRepository driverRepository;

    public DriverService(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }

    public Driver createDriver(Driver driver) {
        return driverRepository.save(driver);
    }

    public List<Driver> getAllDrivers() {
        return driverRepository.findAll();
    }

    public Driver getDriverById(String id) {
        return driverRepository.findById(id)
                .orElseThrow(() -> new com.ridelink.driver.exception.ResourceNotFoundException("Driver not found with ID: " + id));
    }

    public Driver updateDriver(String id, Driver driverUpdates) {
        Driver existingDriver = getDriverById(id);
        
        existingDriver.setName(driverUpdates.getName());
        existingDriver.setPhoneNumber(driverUpdates.getPhoneNumber());
        existingDriver.setLicenseNumber(driverUpdates.getLicenseNumber());
        existingDriver.setAvailabilityStatus(driverUpdates.getAvailabilityStatus());
        existingDriver.setCurrentLocation(driverUpdates.getCurrentLocation());
        
        return driverRepository.save(existingDriver);
    }

    public Driver updateDriverAvailability(String driverId, com.ridelink.drivervehicle.enums.DriverAvailability availability) {
        Driver existingDriver = getDriverById(driverId);
        existingDriver.setAvailabilityStatus(availability);
        return driverRepository.save(existingDriver);
    }

    public void deleteDriver(String id) {
        Driver existingDriver = getDriverById(id);
        driverRepository.delete(existingDriver);
    }
}
