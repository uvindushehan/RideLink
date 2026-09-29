package com.ridemanagement.accountservice.dto.request;

import com.ridemanagement.accountservice.model.UserRole;
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
public class RegisterRequest {

    @NotBlank(message = "First name is required")
        private String firstName;

    @NotBlank(message = "Last name is required")
        private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be a valid email address")
        private String email;

    @NotBlank(message = "Phone number is required")
        private String phoneNumber;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters long")
        private String password;

    @NotNull(message = "User role is required")
        private UserRole role;
}
