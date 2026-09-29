package com.ridemanagement.accountservice.dto.response;

import com.ridemanagement.accountservice.model.AccountStatus;
import com.ridemanagement.accountservice.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Response payload containing account details.
 * Note: Password is intentionally excluded for security.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponse {

        private String id;

        private String firstName;

        private String lastName;

        private String email;

        private String phoneNumber;

        private UserRole role;

        private AccountStatus status;

        private LocalDateTime createdAt;

        private LocalDateTime updatedAt;
}
