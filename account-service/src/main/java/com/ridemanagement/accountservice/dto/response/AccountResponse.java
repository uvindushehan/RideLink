package com.ridemanagement.accountservice.dto.response;

import com.ridemanagement.accountservice.model.AccountStatus;
import com.ridemanagement.accountservice.model.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Response payload containing account details without sensitive credentials")
public class AccountResponse {

    @Schema(description = "Unique MongoDB identifier of the account", example = "654321abcdef0123456789ab")
    private String id;

    @Schema(description = "User's first name", example = "John")
    private String firstName;

    @Schema(description = "User's last name", example = "Doe")
    private String lastName;

    @Schema(description = "User's email address", example = "john.doe@example.com")
    private String email;

    @Schema(description = "User's contact phone number", example = "+1234567890")
    private String phoneNumber;

    @Schema(description = "Role assigned to the user", example = "PASSENGER")
    private UserRole role;

    @Schema(description = "Lifecycle status of the account", example = "ACTIVE")
    private AccountStatus status;

    @Schema(description = "Account creation timestamp")
    private LocalDateTime createdAt;

    @Schema(description = "Account last updated timestamp")
    private LocalDateTime updatedAt;
}
