package com.ridemanagement.accountservice.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Extended API error response returned specifically for {@code MethodArgumentNotValidException}.
 * Includes per-field validation error messages alongside the standard error fields.
 * No stack traces or internal implementation details are ever included.
 */
@Getter
@Builder
public class ValidationErrorResponse {

    /**
     * UTC timestamp at the moment the error was produced.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private final LocalDateTime timestamp;

    /**
     * HTTP status code (always 400 for validation failures).
     */
    private final int status;

    /**
     * Short HTTP status reason phrase ("Bad Request").
     */
    private final String error;

    /**
     * Top-level human-readable description ("Validation failed").
     */
    private final String message;

    /**
     * The request URI path that triggered the error.
     */
    private final String path;

    /**
     * Map of field name → validation failure message for every invalid field.
     * Example: {@code {"email": "must be a valid email address", "password": "size must be between 6 and 2147483647"}}
     */
    private final Map<String, String> validationErrors;
}
