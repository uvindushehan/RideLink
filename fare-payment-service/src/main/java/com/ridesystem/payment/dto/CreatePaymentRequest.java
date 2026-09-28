package com.ridesystem.payment.dto;

import com.ridesystem.payment.model.PaymentMethod;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/**
 * Request DTO for creating a new payment.
 *
 * <p>The fare breakdown is calculated server-side by {@code FareCalculationService}.
 * Clients only supply ride inputs (distance, duration) and payment method.
 *
 * <p>IT3130 AD Group Assignment — RideLink System
 */
public class CreatePaymentRequest {

    @NotBlank(message = "Ride ID must not be blank")
    private String rideId;

    @NotBlank(message = "Passenger ID must not be blank")
    private String passengerId;

    @NotBlank(message = "Driver ID must not be blank")
    private String driverId;

    @NotNull(message = "Distance (km) must not be null")
    @DecimalMin(value = "0.0", message = "Distance must be zero or greater")
    private BigDecimal distanceKm;

    @NotNull(message = "Duration (minutes) must not be null")
    @DecimalMin(value = "0.0", message = "Duration must be zero or greater")
    private BigDecimal durationMinutes;

    @NotNull(message = "Payment method must not be null")
    private PaymentMethod paymentMethod;

    // -----------------------------------------------------------------------
    // Constructors
    // -----------------------------------------------------------------------

    public CreatePaymentRequest() {}

    public CreatePaymentRequest(String rideId, String passengerId, String driverId,
                                BigDecimal distanceKm, BigDecimal durationMinutes,
                                PaymentMethod paymentMethod) {
        this.rideId          = rideId;
        this.passengerId     = passengerId;
        this.driverId        = driverId;
        this.distanceKm      = distanceKm;
        this.durationMinutes = durationMinutes;
        this.paymentMethod   = paymentMethod;
    }

    // -----------------------------------------------------------------------
    // Getters and Setters
    // -----------------------------------------------------------------------

    public String getRideId()                     { return rideId; }
    public void   setRideId(String rideId)        { this.rideId = rideId; }

    public String getPassengerId()                { return passengerId; }
    public void   setPassengerId(String v)        { this.passengerId = v; }

    public String getDriverId()                   { return driverId; }
    public void   setDriverId(String v)           { this.driverId = v; }

    public BigDecimal getDistanceKm()             { return distanceKm; }
    public void       setDistanceKm(BigDecimal v) { this.distanceKm = v; }

    public BigDecimal getDurationMinutes()              { return durationMinutes; }
    public void       setDurationMinutes(BigDecimal v)  { this.durationMinutes = v; }

    public PaymentMethod getPaymentMethod()              { return paymentMethod; }
    public void          setPaymentMethod(PaymentMethod v){ this.paymentMethod = v; }
}
