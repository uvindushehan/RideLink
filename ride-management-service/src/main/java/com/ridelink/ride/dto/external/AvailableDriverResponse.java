package com.ridelink.ride.dto.external;

public class AvailableDriverResponse {

    private String driverId;
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
