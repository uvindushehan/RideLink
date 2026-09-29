package com.ridemanagement.accountservice.exception;

/**
 * Exception thrown when a login attempt fails due to an incorrect email or password.
 * A deliberately generic message is used so that callers cannot distinguish
 * between a non-existent account and a wrong password (preventing user enumeration).
 */
public class InvalidCredentialsException extends RuntimeException {

    public InvalidCredentialsException(String message) {
        super(message);
    }
}
