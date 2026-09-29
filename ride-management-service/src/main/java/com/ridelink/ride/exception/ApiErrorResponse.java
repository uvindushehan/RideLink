package com.ridelink.ride.exception;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Standard error response returned by the Ride Management Service for all error cases")
public class ApiErrorResponse {

    @Schema(description = "Timestamp of when the error occurred", example = "2024-06-01T10:30:00")
    private LocalDateTime timestamp;

    @Schema(description = "HTTP status code", example = "404")
    private int status;

    @Schema(description = "Short HTTP error description", example = "Not Found")
    private String error;

    @Schema(description = "Detailed error message", example = "Ride with ID 64a1f2c3e4b0f1a2b3c4d5e6 was not found")
    private String message;

    @Schema(description = "Request path that triggered the error", example = "/api/rides/64a1f2c3e4b0f1a2b3c4d5e6")
    private String path;

    public ApiErrorResponse() {
    }

    public ApiErrorResponse(int status, String error, String message, String path) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
