package com.ridesystem.payment.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/**
 * Request DTO for initiating a refund on a completed payment.
 *
 * <p>Refund rules enforced by the service layer:
 * <ul>
 *     <li>Payment must be in {@code COMPLETED} status.</li>
 *     <li>Payment must not already be {@code REFUNDED}.</li>
 *     <li>{@code refundAmount} must be greater than zero.</li>
 *     <li>{@code refundAmount} must not exceed the original {@code totalAmount}.</li>
 * </ul>
 *
 * <p>IT3130 AD Group Assignment — RideLink System
 */
public class RefundRequest {

    @NotNull(message = "Refund amount must not be null")
    @DecimalMin(value = "0.01", inclusive = true, message = "Refund amount must be greater than zero")
    private BigDecimal refundAmount;

    // -----------------------------------------------------------------------
    // Constructors
    // -----------------------------------------------------------------------

    public RefundRequest() {}

    public RefundRequest(BigDecimal refundAmount) {
        this.refundAmount = refundAmount;
    }

    // -----------------------------------------------------------------------
    // Getters and Setters
    // -----------------------------------------------------------------------

    public BigDecimal getRefundAmount()             { return refundAmount; }
    public void       setRefundAmount(BigDecimal v) { this.refundAmount = v; }
}
