package com.ridelink.ride.dto.external;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Driver availability record returned by the Driver & Vehicle Service")
public class AvailableDriverResponse {

    @Schema(description = "Unique identifier of the available driver", example = "driver-007")
    private String driverId;

    @Schema(description = "Current availability status of the driver", example = "AVAILABLE")
    private String availabilityStatus;

    public AvailableDriverResponse() {
    }

    public AvailableDriverResponse(String driverId, String availabilityStatus) {
        this.driverId = driverId;
        this.availabilityStatus = availabilityStatus;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getAvailabilityStatus() {
        return availabilityStatus;
    }

    public void setAvailabilityStatus(String availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }
}
