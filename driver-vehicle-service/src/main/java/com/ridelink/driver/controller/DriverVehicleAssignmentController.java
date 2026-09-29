package com.ridelink.driver.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ridelink.driver.document.DriverVehicleAssignment;
import com.ridelink.driver.service.DriverVehicleAssignmentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/driver-vehicle-assignments")
@Tag(name = "Driver-Vehicle Assignments", description = "Endpoints for managing driver-vehicle assignments")
public class DriverVehicleAssignmentController {

    private final DriverVehicleAssignmentService assignmentService;

    public DriverVehicleAssignmentController(DriverVehicleAssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    @PostMapping
    @Operation(summary = "Create a new driver-vehicle assignment")
    public ResponseEntity<?> createAssignment(@jakarta.validation.Valid @RequestBody DriverVehicleAssignment assignment) {
        DriverVehicleAssignment created = assignmentService.createAssignment(
            assignment.getDriverId(), 
            assignment.getVehicleId()
        );
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get all driver-vehicle assignments")
    public ResponseEntity<List<DriverVehicleAssignment>> getAllAssignments() {
        return ResponseEntity.ok(assignmentService.getAllAssignments());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get an assignment by ID")
    public ResponseEntity<?> getAssignmentById(@PathVariable String id) {
        DriverVehicleAssignment assignment = assignmentService.getAssignmentById(id);
        return ResponseEntity.ok(assignment);
    }

    @GetMapping("/driver/{driverId}")
    @Operation(summary = "Get assignments for a specific driver")
    public ResponseEntity<List<DriverVehicleAssignment>> getAssignmentsByDriverId(@PathVariable String driverId) {
        return ResponseEntity.ok(assignmentService.getAssignmentsByDriverId(driverId));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deactivate an assignment by ID")
    public ResponseEntity<?> deactivateAssignment(@PathVariable String id) {
        assignmentService.deactivateAssignment(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
