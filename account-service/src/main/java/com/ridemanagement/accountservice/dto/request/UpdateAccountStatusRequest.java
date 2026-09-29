package com.ridemanagement.accountservice.dto.request;

import com.ridemanagement.accountservice.model.AccountStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request payload for updating an account's status")
public class UpdateAccountStatusRequest {

    @NotNull(message = "Account status is required")
    @Schema(description = "The new status to apply to the account", example = "SUSPENDED", allowableValues = {"ACTIVE", "INACTIVE", "SUSPENDED"})
    private AccountStatus status;
}
