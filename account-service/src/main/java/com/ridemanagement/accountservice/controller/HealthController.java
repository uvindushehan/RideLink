package com.ridemanagement.accountservice.controller;

import com.ridemanagement.accountservice.dto.response.HealthResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/accounts")
public class HealthController {

    @GetMapping("/health")
            public ResponseEntity<HealthResponseDto> getHealth() {
        HealthResponseDto response = HealthResponseDto.builder()
                .service("Account Service")
                .status("UP")
                .build();
        return ResponseEntity.ok(response);
    }
}
