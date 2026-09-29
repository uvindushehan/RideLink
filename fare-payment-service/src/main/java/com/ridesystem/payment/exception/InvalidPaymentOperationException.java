package com.ridesystem.payment.exception;

/**
 * Thrown when a payment operation is requested that violates the payment
 * lifecycle rules (e.g., refunding a non-COMPLETED payment, or updating
 * a status with an illegal transition).
 *
 */
public class InvalidPaymentOperationException extends RuntimeException {

    public InvalidPaymentOperationException(String message) {
        super(message);
    }

    /**
     * Thrown when a refund is attempted on a payment that is not COMPLETED.
     */
    public static InvalidPaymentOperationException refundNotAllowed(String currentStatus) {
        return new InvalidPaymentOperationException(
                "Only COMPLETED payments can be refunded. Current status: " + currentStatus
        );
    }

    /**
     * Thrown when a refund is attempted on a payment that is already REFUNDED.
     */
    public static InvalidPaymentOperationException alreadyRefunded() {
        return new InvalidPaymentOperationException(
                "This payment has already been refunded."
        );
    }

    /**
     * Thrown when the requested refund amount exceeds the original total amount.
     */
    public static InvalidPaymentOperationException refundExceedsTotal() {
        return new InvalidPaymentOperationException(
                "Refund amount must not exceed the original total payment amount."
        );
    }
}
