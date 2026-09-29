package com.ridemanagement.accountservice.dto.request;

import com.ridemanagement.accountservice.model.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request payload for new user account registration.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request payload for new user account registration")
public class RegisterRequest {

    @NotBlank(message = "First name is required")
    @Schema(description = "User's first name", example = "John")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Schema(description = "User's last name", example = "Doe")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be a valid email address")
    @Schema(description = "User's unique email address", example = "john.doe@example.com")
    private String email;

    @NotBlank(message = "Phone number is required")
    @Schema(description = "User's unique phone number", example = "+1234567890")
    private String phoneNumber;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    @Schema(description = "User's password (minimum 6 characters)", example = "SecurePass123!")
    private String password;

    @NotNull(message = "User role is required")
    @Schema(description = "Role assigned to the user", example = "PASSENGER")
    private UserRole role;
}
