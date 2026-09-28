package com.ridemanagement.accountservice.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request payload for updating account profile information.
 * Notice: Immutable or sensitive fields (id, email, password, role, status, createdAt, updatedAt) are excluded.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request payload for updating user profile details")
public class UpdateAccountRequest {

    @NotBlank(message = "First name is required")
    @Schema(description = "User's first name", example = "John")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Schema(description = "User's last name", example = "Doe")
    private String lastName;

    @NotBlank(message = "Phone number is required")
    @Schema(description = "User's contact phone number", example = "+1234567890")
    private String phoneNumber;
}
