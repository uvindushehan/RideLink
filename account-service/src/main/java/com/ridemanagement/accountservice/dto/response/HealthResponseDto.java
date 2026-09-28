package com.ridemanagement.accountservice.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response payload for Account Service health check")
public class HealthResponseDto {

    @Schema(description = "Name of the microservice", example = "Account Service")
    private String service;

    @Schema(description = "Current operational status", example = "UP")
    private String status;
}
