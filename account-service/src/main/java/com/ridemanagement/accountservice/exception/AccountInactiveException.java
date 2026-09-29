package com.ridemanagement.accountservice.exception;

/**
 * Exception thrown when a login attempt is made against an account
 * whose status is {@code INACTIVE}.
 */
public class AccountInactiveException extends RuntimeException {

    public AccountInactiveException(String message) {
        super(message);
    }
}
