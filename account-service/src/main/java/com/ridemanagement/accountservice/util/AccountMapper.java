package com.ridemanagement.accountservice.util;

import com.ridemanagement.accountservice.dto.request.RegisterRequest;
import com.ridemanagement.accountservice.dto.response.AccountResponse;
import com.ridemanagement.accountservice.model.Account;
import com.ridemanagement.accountservice.model.AccountStatus;

import java.time.LocalDateTime;

/**
 * Reusable mapping utility to convert between Account entities and DTOs.
 */
public final class AccountMapper {

    private AccountMapper() {
        // Private constructor to prevent instantiation
    }

    /**
     * Maps RegisterRequest to an Account domain model.
     * Initializes status to ACTIVE and timestamps to current time.
     *
     * @param request the registration request payload
     * @return populated Account entity
     */
    public static Account toAccount(RegisterRequest request) {
        if (request == null) {
            return null;
        }

        LocalDateTime now = LocalDateTime.now();
        return Account.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .password(request.getPassword())
                .role(request.getRole())
                .status(AccountStatus.ACTIVE)
                .createdAt(now)
                .updatedAt(now)
                .build();
    }

    /**
     * Maps an Account domain model to an AccountResponse DTO.
     * Password is explicitly omitted for security.
     *
     * @param account the account entity
     * @return safe AccountResponse DTO
     */
    public static AccountResponse toAccountResponse(Account account) {
        if (account == null) {
            return null;
        }

        return AccountResponse.builder()
                .id(account.getId())
                .firstName(account.getFirstName())
                .lastName(account.getLastName())
                .email(account.getEmail())
                .phoneNumber(account.getPhoneNumber())
                .role(account.getRole())
                .status(account.getStatus())
                .createdAt(account.getCreatedAt())
                .updatedAt(account.getUpdatedAt())
                .build();
    }
}
