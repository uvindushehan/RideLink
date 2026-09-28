package com.ridelink.driver.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ridelink.driver.document.DriverVehicleAssignment;
import com.ridelink.driver.repository.DriverRepository;
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
        if (!driverRepository.existsById(driverId)) {
            throw new RuntimeException("Driver not found with id: " + driverId);
        }
        if (!vehicleRepository.existsById(vehicleId)) {
            throw new RuntimeException("Vehicle not found with id: " + vehicleId);
        }

        DriverVehicleAssignment assignment = new DriverVehicleAssignment(
                driverId,
                vehicleId,
                LocalDateTime.now(),
                true
        );

        return assignmentRepository.save(assignment);
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
        return assignmentRepository.save(assignment);
    }
}
