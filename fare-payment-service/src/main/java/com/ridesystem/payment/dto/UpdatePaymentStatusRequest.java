package com.ridesystem.payment.dto;

import com.ridesystem.payment.model.PaymentStatus;
import jakarta.validation.constraints.NotNull;

/**
 * Request DTO for updating the status of an existing payment.
 *
 * <p>IT3130 AD Group Assignment — RideLink System
 */
public class UpdatePaymentStatusRequest {

    @NotNull(message = "Payment status must not be null")
    private PaymentStatus paymentStatus;

    // -----------------------------------------------------------------------
    // Constructors
    // -----------------------------------------------------------------------

    public UpdatePaymentStatusRequest() {}

    public UpdatePaymentStatusRequest(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    // -----------------------------------------------------------------------
    // Getters and Setters
    // -----------------------------------------------------------------------

    public PaymentStatus getPaymentStatus()                { return paymentStatus; }
    public void          setPaymentStatus(PaymentStatus v) { this.paymentStatus = v; }
}
