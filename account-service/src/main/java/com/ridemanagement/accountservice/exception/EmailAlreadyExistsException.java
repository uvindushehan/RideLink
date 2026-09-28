package com.ridemanagement.accountservice.exception;

/**
 * Exception thrown when attempting to register or update with an email that is already registered.
 */
public class EmailAlreadyExistsException extends RuntimeException {

    public EmailAlreadyExistsException(String message) {
        super(message);
    }
}
