package com.ridelink.drivervehicle.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.ridelink.drivervehicle.enums.DriverAvailability;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Document(collection = "drivers")
public class Driver {

    @Id
    private String id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Invalid phone number format")
    private String phoneNumber;

    @NotBlank(message = "License number is required")
    private String licenseNumber;

    @NotNull(message = "Availability status is required")
    private DriverAvailability availabilityStatus;

    @NotNull(message = "Current location is required")
    private Location currentLocation;

    public Driver() {}

    public Driver(String name, String phoneNumber, String licenseNumber, DriverAvailability availabilityStatus, Location currentLocation) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.licenseNumber = licenseNumber;
        this.availabilityStatus = availabilityStatus;
        this.currentLocation = currentLocation;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getLicenseNumber() { return licenseNumber; }
    public void setLicenseNumber(String licenseNumber) { this.licenseNumber = licenseNumber; }

    public DriverAvailability getAvailabilityStatus() { return availabilityStatus; }
    public void setAvailabilityStatus(DriverAvailability availabilityStatus) { this.availabilityStatus = availabilityStatus; }

    public Location getCurrentLocation() { return currentLocation; }
    public void setCurrentLocation(Location currentLocation) { this.currentLocation = currentLocation; }

    public static class Location {
        @NotNull(message = "Latitude is required")
        private Double latitude;

        @NotNull(message = "Longitude is required")
        private Double longitude;

        public Location() {}

        public Location(Double latitude, Double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }

        public Double getLatitude() { return latitude; }
        public void setLatitude(Double latitude) { this.latitude = latitude; }

        public Double getLongitude() { return longitude; }
        public void setLongitude(Double longitude) { this.longitude = longitude; }
    }
}
