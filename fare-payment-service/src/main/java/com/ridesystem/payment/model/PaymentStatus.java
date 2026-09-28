package com.ridesystem.payment.model;

/**
 * Lifecycle statuses for a payment within the Fare Payment Service.
 *
 * <p>State transitions:
 * <pre>
 *   PENDING ──► COMPLETED
 *           └──► FAILED
 *   COMPLETED ──► REFUNDED
 * </pre>
 *
 * <p>IT3130 AD Group Assignment — RideLink System
 */
public enum PaymentStatus {

    /**
     * Payment has been initiated but not yet processed.
     */
    PENDING,

    /**
     * Payment was successfully processed.
     */
    COMPLETED,

    /**
     * Payment processing failed.
     */
    FAILED,

    /**
     * Payment was successfully refunded.
     */
    REFUNDED
}
