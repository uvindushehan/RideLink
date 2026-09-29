package com.ridelink.driver.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ridelink.driver.document.Vehicle;
import com.ridelink.driver.repository.VehicleRepository;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public Vehicle createVehicle(Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }

    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    public Vehicle getVehicleById(String id) {
        return vehicleRepository.findById(id)
                .orElseThrow(() -> new com.ridelink.driver.exception.ResourceNotFoundException("Vehicle not found with ID: " + id));
    }

    public Vehicle updateVehicle(String id, Vehicle vehicleUpdates) {
        Vehicle existingVehicle = getVehicleById(id);
        
        existingVehicle.setRegistrationNumber(vehicleUpdates.getRegistrationNumber());
        existingVehicle.setBrand(vehicleUpdates.getBrand());
        existingVehicle.setModel(vehicleUpdates.getModel());
        existingVehicle.setVehicleType(vehicleUpdates.getVehicleType());
        existingVehicle.setColor(vehicleUpdates.getColor());
        existingVehicle.setManufactureYear(vehicleUpdates.getManufactureYear());
        existingVehicle.setSeatingCapacity(vehicleUpdates.getSeatingCapacity());
        existingVehicle.setStatus(vehicleUpdates.getStatus());
        existingVehicle.setUpdatedAt(vehicleUpdates.getUpdatedAt());
        
        return vehicleRepository.save(existingVehicle);
    }

    public Vehicle updateVehicleStatus(String vehicleId, com.ridelink.driver.enums.VehicleStatus status) {
        Vehicle existingVehicle = getVehicleById(vehicleId);
        existingVehicle.setStatus(status);
        existingVehicle.setUpdatedAt(java.time.LocalDateTime.now());
        return vehicleRepository.save(existingVehicle);
    }

    public void deleteVehicle(String id) {
        Vehicle existingVehicle = getVehicleById(id);
        vehicleRepository.delete(existingVehicle);
    }
}
