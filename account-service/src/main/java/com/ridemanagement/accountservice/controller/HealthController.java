package com.ridemanagement.accountservice.controller;

import com.ridemanagement.accountservice.dto.response.HealthResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/accounts")
@Tag(name = "Account Service Health", description = "Endpoints for monitoring Account Service status")
public class HealthController {

    @GetMapping("/health")
    @Operation(
        summary = "Check Account Service Health",
        description = "Returns the operational status and service identifier to verify the service is running"
    )
    @ApiResponse(responseCode = "200", description = "Account Service is UP and running")
    public ResponseEntity<HealthResponseDto> getHealth() {
        HealthResponseDto response = HealthResponseDto.builder()
                .service("Account Service")
                .status("UP")
                .build();
        return ResponseEntity.ok(response);
    }
}
