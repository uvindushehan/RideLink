package com.ridemanagement.accountservice.exception;

/**
 * Exception thrown when attempting to register or update with a phone number that is already registered.
 */
public class PhoneNumberAlreadyExistsException extends RuntimeException {

    public PhoneNumberAlreadyExistsException(String message) {
        super(message);
    }
}
