package com.ridemanagement.accountservice.service;

import com.ridemanagement.accountservice.dto.request.LoginRequest;
import com.ridemanagement.accountservice.dto.request.RegisterRequest;
import com.ridemanagement.accountservice.dto.request.UpdateAccountRequest;
import com.ridemanagement.accountservice.dto.response.AccountResponse;
import com.ridemanagement.accountservice.dto.response.LoginResponse;

import java.util.List;

/**
 * Service interface for core account management operations.
 */
public interface AccountService {

    /**
     * Registers a new user account.
     *
     * @param request the registration details
     * @return the created account details
     */
    AccountResponse registerAccount(RegisterRequest request);

    /**
     * Retrieves an account by its unique identifier.
     *
     * @param id the MongoDB account ID
     * @return the account details
     */
    AccountResponse getAccountById(String id);

    /**
     * Retrieves an account by its email address.
     *
     * @param email the user email
     * @return the account details
     */
    AccountResponse getAccountByEmail(String email);

    /**
     * Updates profile details of an existing account.
     *
     * @param id the MongoDB account ID
     * @param request the update details
     * @return the updated account details
     */
    AccountResponse updateAccount(String id, UpdateAccountRequest request);

    /**
     * Retrieves all user accounts.
     *
     * @return list of account responses
     */
    List<AccountResponse> getAllAccounts();

    /**
     * Deactivates an account by setting status to INACTIVE.
     *
     * @param id the MongoDB account ID
     */
    void deactivateAccount(String id);

    /**
     * Authenticates a user by email and password.
     *
     * <p>Verifies the provided plain-text password against the stored BCrypt hash,
     * then checks that the account status is {@code ACTIVE}.
     * Returns a temporary {@link LoginResponse} with {@code token = null} until
     * JWT generation is implemented in a later step.
     *
     * @param request the login credentials
     * @return a {@link LoginResponse} containing account details and a null JWT token
     */
    LoginResponse login(LoginRequest request);
}
