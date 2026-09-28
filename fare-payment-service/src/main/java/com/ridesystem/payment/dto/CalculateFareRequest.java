package com.ridesystem.payment.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Request DTO for previewing fare calculation without creating a payment.
 *
 * <p>IT3130 AD Group Assignment — RideLink System
 */
public class CalculateFareRequest {

    @NotNull(message = "Distance (km) must not be null")
    @DecimalMin(value = "0.0", message = "Distance must be zero or greater")
    private BigDecimal distanceKm;

    @NotNull(message = "Duration (minutes) must not be null")
    @DecimalMin(value = "0.0", message = "Duration must be zero or greater")
    private BigDecimal durationMinutes;

    public CalculateFareRequest() {}

    public CalculateFareRequest(BigDecimal distanceKm, BigDecimal durationMinutes) {
        this.distanceKm = distanceKm;
        this.durationMinutes = durationMinutes;
    }

    public BigDecimal getDistanceKm() { return distanceKm; }
    public void setDistanceKm(BigDecimal distanceKm) { this.distanceKm = distanceKm; }

    public BigDecimal getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(BigDecimal durationMinutes) { this.durationMinutes = durationMinutes; }
}
