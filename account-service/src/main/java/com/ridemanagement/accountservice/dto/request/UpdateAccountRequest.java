package com.ridemanagement.accountservice.dto.request;

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
public class UpdateAccountRequest {

    @NotBlank(message = "First name is required")
        private String firstName;

    @NotBlank(message = "Last name is required")
        private String lastName;

    @NotBlank(message = "Phone number is required")
        private String phoneNumber;
}
