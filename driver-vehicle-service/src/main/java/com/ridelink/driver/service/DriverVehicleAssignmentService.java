package com.ridelink.driver.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ridelink.driver.document.DriverVehicleAssignment;
import com.ridelink.driver.document.Vehicle;
import com.ridelink.driver.enums.VehicleStatus;
import com.ridelink.drivervehicle.document.Driver;
import com.ridelink.drivervehicle.repository.DriverRepository;
import com.ridelink.driver.repository.DriverVehicleAssignmentRepository;
import com.ridelink.driver.repository.VehicleRepository;

@Service
public class DriverVehicleAssignmentService {

    private final DriverVehicleAssignmentRepository assignmentRepository;
    private final DriverRepository driverRepository;
    private final VehicleRepository vehicleRepository;

    public DriverVehicleAssignmentService(DriverVehicleAssignmentRepository assignmentRepository,
                                          DriverRepository driverRepository,
                                          VehicleRepository vehicleRepository) {
        this.assignmentRepository = assignmentRepository;
        this.driverRepository = driverRepository;
        this.vehicleRepository = vehicleRepository;
    }

    public DriverVehicleAssignment createAssignment(String driverId, String vehicleId) {
        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new RuntimeException("Driver not found with id: " + driverId));
                
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new RuntimeException("Vehicle not found with id: " + vehicleId));

        if (driver.getAvailabilityStatus() == com.ridelink.drivervehicle.enums.DriverAvailability.INACTIVE || 
            driver.getAvailabilityStatus() == com.ridelink.drivervehicle.enums.DriverAvailability.SUSPENDED) {
            throw new RuntimeException("Cannot assign driver with status: " + driver.getAvailabilityStatus());
        }

        if (vehicle.getStatus() == VehicleStatus.MAINTENANCE || vehicle.getStatus() == VehicleStatus.INACTIVE) {
            throw new RuntimeException("Cannot assign vehicle with status: " + vehicle.getStatus());
        }

        if (assignmentRepository.existsByDriverIdAndActive(driverId, true)) {
            throw new RuntimeException("Driver already has an active vehicle assignment.");
        }

        if (assignmentRepository.existsByVehicleIdAndActive(vehicleId, true)) {
            throw new RuntimeException("Vehicle already has an active driver assignment.");
        }

        DriverVehicleAssignment assignment = new DriverVehicleAssignment(
                driverId,
                vehicleId,
                LocalDateTime.now(),
                true
        );
        DriverVehicleAssignment savedAssignment = assignmentRepository.save(assignment);

        driver.setAvailabilityStatus(com.ridelink.drivervehicle.enums.DriverAvailability.BUSY);
        driverRepository.save(driver);

        vehicle.setStatus(VehicleStatus.IN_USE);
        vehicleRepository.save(vehicle);

        return savedAssignment;
    }

    public List<DriverVehicleAssignment> getAllAssignments() {
        return assignmentRepository.findAll();
    }

    public DriverVehicleAssignment getAssignmentById(String id) {
        return assignmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Assignment not found with id: " + id));
    }

    public List<DriverVehicleAssignment> getAssignmentsByDriverId(String driverId) {
        return assignmentRepository.findByDriverId(driverId);
    }

    public DriverVehicleAssignment deactivateAssignment(String id) {
        DriverVehicleAssignment assignment = getAssignmentById(id);
        assignment.setActive(false);
        DriverVehicleAssignment savedAssignment = assignmentRepository.save(assignment);
        
        Driver driver = driverRepository.findById(assignment.getDriverId())
                .orElseThrow(() -> new RuntimeException("Driver not found with id: " + assignment.getDriverId()));
        driver.setAvailabilityStatus(com.ridelink.drivervehicle.enums.DriverAvailability.AVAILABLE);
        driverRepository.save(driver);
        
        Vehicle vehicle = vehicleRepository.findById(assignment.getVehicleId())
                .orElseThrow(() -> new RuntimeException("Vehicle not found with id: " + assignment.getVehicleId()));
        vehicle.setStatus(VehicleStatus.AVAILABLE);
        vehicleRepository.save(vehicle);
        
        return savedAssignment;
    }
}
