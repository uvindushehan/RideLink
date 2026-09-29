package com.ridelink.driver.controller;

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

import com.ridelink.driver.document.Vehicle;
import com.ridelink.driver.service.VehicleService;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @PostMapping
        public ResponseEntity<Vehicle> createVehicle(@jakarta.validation.Valid @RequestBody Vehicle vehicle) {
        Vehicle createdVehicle = vehicleService.createVehicle(vehicle);
        return new ResponseEntity<>(createdVehicle, HttpStatus.CREATED);
    }

    @GetMapping
        public ResponseEntity<List<Vehicle>> getAllVehicles() {
        return ResponseEntity.ok(vehicleService.getAllVehicles());
    }

    @GetMapping("/{id}")
        public ResponseEntity<Vehicle> getVehicleById(@PathVariable String id) {
        Vehicle vehicle = vehicleService.getVehicleById(id);
        return ResponseEntity.ok(vehicle);
    }

    @PutMapping("/{id}")
        public ResponseEntity<Vehicle> updateVehicle(@PathVariable String id, @jakarta.validation.Valid @RequestBody Vehicle vehicleUpdates) {
        Vehicle updatedVehicle = vehicleService.updateVehicle(id, vehicleUpdates);
        return ResponseEntity.ok(updatedVehicle);
    }

    @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteVehicle(@PathVariable String id) {
        vehicleService.deleteVehicle(id);
        return ResponseEntity.noContent().build();
    }

    @org.springframework.web.bind.annotation.PatchMapping("/{id}/status")
        public ResponseEntity<Vehicle> updateVehicleStatus(@PathVariable String id, @RequestBody com.ridelink.driver.enums.VehicleStatus status) {
        Vehicle updatedVehicle = vehicleService.updateVehicleStatus(id, status);
        return ResponseEntity.ok(updatedVehicle);
    }
}
