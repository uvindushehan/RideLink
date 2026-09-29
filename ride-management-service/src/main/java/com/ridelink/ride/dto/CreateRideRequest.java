package com.ridelink.ride.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Request body for creating a new ride")
public class CreateRideRequest {

    @Schema(description = "Unique identifier of the passenger requesting the ride", example = "passenger-001")
    @NotBlank(message = "Passenger ID must not be blank")
    private String passengerId;

    @Schema(description = "Pickup location for the ride")
    @NotNull(message = "Pickup location must not be null")
    @Valid
    private LocationDto pickupLocation;

    @Schema(description = "Destination location for the ride")
    @NotNull(message = "Destination location must not be null")
    @Valid
    private LocationDto destinationLocation;

    public CreateRideRequest() {
    }

    public String getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(String passengerId) {
        this.passengerId = passengerId;
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
}
