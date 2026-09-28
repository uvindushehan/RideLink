package com.ridemanagement.accountservice.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * Standardized API error response returned for all handled exceptions.
 * No stack traces or internal implementation details are ever included.
 */
@Getter
@Builder
public class ApiErrorResponse {

    /**
     * UTC timestamp at the moment the error was produced.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private final LocalDateTime timestamp;

    /**
     * HTTP status code (e.g. 404, 409, 500).
     */
    private final int status;

    /**
     * Short HTTP status reason phrase (e.g. "Not Found", "Conflict").
     */
    private final String error;

    /**
     * Human-readable description of what went wrong.
     */
    private final String message;

    /**
     * The request URI path that triggered the error.
     */
    private final String path;
}
