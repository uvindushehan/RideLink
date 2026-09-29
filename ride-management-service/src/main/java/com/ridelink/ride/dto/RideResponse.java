package com.ridelink.ride.dto;

import com.ridelink.ride.enums.RideStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Full details of a ride returned by the Ride Management Service")
public class RideResponse {

    @Schema(description = "Unique identifier of the ride (MongoDB ObjectId)", example = "64a1f2c3e4b0f1a2b3c4d5e6")
    private String id;

    @Schema(description = "Unique identifier of the passenger who requested the ride", example = "passenger-001")
    private String passengerId;

    @Schema(description = "Unique identifier of the assigned driver (null if no driver has been assigned yet)", example = "driver-007", nullable = true)
    private String driverId;

    @Schema(description = "Pickup location of the ride")
    private LocationDto pickupLocation;

    @Schema(description = "Destination location of the ride")
    private LocationDto destinationLocation;

    @Schema(description = "Current lifecycle status of the ride",
            allowableValues = {"REQUESTED", "ASSIGNED", "ACCEPTED", "IN_PROGRESS", "COMPLETED", "CANCELLED"},
            example = "REQUESTED")
    private RideStatus status;

    @Schema(description = "Timestamp when the ride was created", example = "2024-06-01T10:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "Timestamp when the ride was last updated", example = "2024-06-01T10:45:00")
    private LocalDateTime updatedAt;

    public RideResponse() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(String passengerId) {
        this.passengerId = passengerId;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public LocationDto getPickupLocation() {
        return pickupLocation;
    }

    public void setPickupLocation(LocationDto pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    public LocationDto getDestinationLocation() {
        return destinationLocation;
    }

    public void setDestinationLocation(LocationDto destinationLocation) {
        this.destinationLocation = destinationLocation;
    }

    public RideStatus getStatus() {
        return status;
    }

    public void setStatus(RideStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
