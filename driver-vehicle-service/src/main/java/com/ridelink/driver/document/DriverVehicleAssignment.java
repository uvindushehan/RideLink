package com.ridelink.driver.document;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "driver_vehicle_assignments")
public class DriverVehicleAssignment {

    @Id
    private String id;

    private String driverId;
    private String vehicleId;
    private LocalDateTime assignedAt;
    private boolean active;

    public DriverVehicleAssignment() {
    }

    public DriverVehicleAssignment(String driverId, String vehicleId, LocalDateTime assignedAt, boolean active) {
        this.driverId = driverId;
        this.vehicleId = vehicleId;
        this.assignedAt = assignedAt;
        this.active = active;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public LocalDateTime getAssignedAt() {
        return assignedAt;
    }

    public void setAssignedAt(LocalDateTime assignedAt) {
        this.assignedAt = assignedAt;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
