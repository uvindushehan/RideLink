package com.ridesystem.payment.exception;

/**
 * Thrown when a Payment document cannot be found by the given identifier.
 *
 */
public class PaymentNotFoundException extends RuntimeException {

    public PaymentNotFoundException(String message) {
        super(message);
    }

    /**
     * Convenience factory — thrown when searching by Payment ID.
     */
    public static PaymentNotFoundException byId(String paymentId) {
        return new PaymentNotFoundException("Payment not found with id: " + paymentId);
    }

    /**
     * Convenience factory — thrown when searching by Ride ID.
     */
    public static PaymentNotFoundException byRideId(String rideId) {
        return new PaymentNotFoundException("Payment not found for ride: " + rideId);
    }
}
