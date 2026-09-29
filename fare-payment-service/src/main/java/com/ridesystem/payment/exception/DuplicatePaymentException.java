package com.ridesystem.payment.exception;

/**
 * Thrown when a payment creation is attempted for a ride that already has
 * an existing payment document.
 *
 * <p>The {@code rideId} field carries a unique MongoDB index, so this exception
 * is also the application-level guard before that constraint is hit at the DB level.
 *
 */
public class DuplicatePaymentException extends RuntimeException {

    public DuplicatePaymentException(String message) {
        super(message);
    }

    /**
     * Convenience factory — thrown when a ride already has a payment.
     */
    public static DuplicatePaymentException forRide(String rideId) {
        return new DuplicatePaymentException(
                "Payment already exists for ride: " + rideId +
                ". Duplicate payments for the same ride are not allowed."
        );
    }
}
