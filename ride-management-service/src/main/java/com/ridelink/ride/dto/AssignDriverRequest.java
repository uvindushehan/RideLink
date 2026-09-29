package com.ridelink.ride.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Request body for manually assigning a driver to a ride")
public class AssignDriverRequest {

    @Schema(description = "Unique identifier of the driver to assign", example = "driver-007")
    @NotBlank(message = "Driver ID must not be blank")
    private String driverId;

    public AssignDriverRequest() {
    }

    public AssignDriverRequest(String driverId) {
        this.driverId = driverId;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }
}
