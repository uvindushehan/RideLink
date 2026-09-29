package com.ridelink.drivervehicle.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ridelink.drivervehicle.document.Driver;
import com.ridelink.drivervehicle.service.DriverService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/drivers")
@Tag(name = "Driver Management", description = "Endpoints for managing drivers")
public class DriverController {

    private final DriverService driverService;

    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }

    @PostMapping
    @Operation(summary = "Create a new driver")
    public ResponseEntity<Driver> createDriver(@jakarta.validation.Valid @RequestBody Driver driver) {
        Driver createdDriver = driverService.createDriver(driver);
        return new ResponseEntity<>(createdDriver, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get all drivers")
    public ResponseEntity<List<Driver>> getAllDrivers() {
        return ResponseEntity.ok(driverService.getAllDrivers());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a driver by ID")
    public ResponseEntity<Driver> getDriverById(@PathVariable String id) {
        Driver driver = driverService.getDriverById(id);
        return ResponseEntity.ok(driver);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing driver")
    public ResponseEntity<Driver> updateDriver(@PathVariable String id, @jakarta.validation.Valid @RequestBody Driver driverUpdates) {
        Driver updatedDriver = driverService.updateDriver(id, driverUpdates);
        return ResponseEntity.ok(updatedDriver);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a driver by ID")
    public ResponseEntity<Void> deleteDriver(@PathVariable String id) {
        driverService.deleteDriver(id);
        return ResponseEntity.noContent().build();
    }

    @org.springframework.web.bind.annotation.PatchMapping("/{id}/availability")
    @Operation(summary = "Update driver availability status")
    public ResponseEntity<Driver> updateDriverAvailability(@PathVariable String id, @RequestBody com.ridelink.drivervehicle.enums.DriverAvailability availability) {
        Driver updatedDriver = driverService.updateDriverAvailability(id, availability);
        return ResponseEntity.ok(updatedDriver);
    }
}
