package com.ridemanagement.accountservice.exception;

/**
 * Exception thrown when a login attempt is made against an account
 * whose status is {@code SUSPENDED}.
 */
public class AccountSuspendedException extends RuntimeException {

    public AccountSuspendedException(String message) {
        super(message);
    }
}
