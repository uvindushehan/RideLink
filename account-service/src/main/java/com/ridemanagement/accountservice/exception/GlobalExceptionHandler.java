package com.ridemanagement.accountservice.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Centralized exception handler for all Account Service REST controllers.
 *
 * <p>Intercepts known domain exceptions and maps them to consistent, structured
 * JSON error responses. No Java stack traces are ever returned to API clients.
 *
 * <p>Handled exceptions and their HTTP status codes:
 * <ul>
 *   <li>{@link ResourceNotFoundException}           → 404 Not Found</li>
 *   <li>{@link EmailAlreadyExistsException}         → 409 Conflict</li>
 *   <li>{@link PhoneNumberAlreadyExistsException}   → 409 Conflict</li>
 *   <li>{@link MethodArgumentNotValidException}     → 400 Bad Request (with per-field errors)</li>
 *   <li>{@link IllegalArgumentException}            → 400 Bad Request</li>
 *   <li>{@link Exception}                           → 500 Internal Server Error</li>
 * </ul>
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // -------------------------------------------------------------------------
    // 404 Not Found
    // -------------------------------------------------------------------------

    /**
     * Handles lookups for accounts that do not exist in MongoDB.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleResourceNotFoundException(
            ResourceNotFoundException ex,
            HttpServletRequest request) {

        log.warn("Resource not found at {}: {}", request.getRequestURI(), ex.getMessage());

        ApiErrorResponse response = ApiErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    // -------------------------------------------------------------------------
    // 409 Conflict
    // -------------------------------------------------------------------------

    /**
     * Handles registration attempts with an email address that is already in use.
     */
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ApiErrorResponse> handleEmailAlreadyExistsException(
            EmailAlreadyExistsException ex,
            HttpServletRequest request) {

        log.warn("Email conflict at {}: {}", request.getRequestURI(), ex.getMessage());

        ApiErrorResponse response = ApiErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.CONFLICT.value())
                .error(HttpStatus.CONFLICT.getReasonPhrase())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    /**
     * Handles registration or update attempts with a phone number that is already in use.
     */
    @ExceptionHandler(PhoneNumberAlreadyExistsException.class)
    public ResponseEntity<ApiErrorResponse> handlePhoneNumberAlreadyExistsException(
            PhoneNumberAlreadyExistsException ex,
            HttpServletRequest request) {

        log.warn("Phone number conflict at {}: {}", request.getRequestURI(), ex.getMessage());

        ApiErrorResponse response = ApiErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.CONFLICT.value())
                .error(HttpStatus.CONFLICT.getReasonPhrase())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    // -------------------------------------------------------------------------
    // 400 Bad Request — Bean Validation
    // -------------------------------------------------------------------------

    /**
     * Handles {@code @Valid} / {@code @Validated} constraint violations on request bodies.
     *
     * <p>Returns a {@link ValidationErrorResponse} that includes per-field error messages
     * in a {@code validationErrors} map so API consumers can display meaningful form errors.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        log.warn("Validation failed at {}: {} field error(s)", request.getRequestURI(),
                ex.getBindingResult().getFieldErrorCount());

        // Collect all field → message pairs; LinkedHashMap preserves insertion order
        Map<String, String> fieldErrors = new LinkedHashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            // Do not overwrite the first error for a field if there are multiple violations
            fieldErrors.putIfAbsent(fieldError.getField(), fieldError.getDefaultMessage());
        }

        ValidationErrorResponse response = ValidationErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message("Validation failed")
                .path(request.getRequestURI())
                .validationErrors(fieldErrors)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // -------------------------------------------------------------------------
    // 400 Bad Request — Illegal Argument
    // -------------------------------------------------------------------------

    /**
     * Handles programmatic argument violations (e.g. invalid enum values passed as strings).
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> handleIllegalArgumentException(
            IllegalArgumentException ex,
            HttpServletRequest request) {

        log.warn("Illegal argument at {}: {}", request.getRequestURI(), ex.getMessage());

        ApiErrorResponse response = ApiErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // -------------------------------------------------------------------------
    // 500 Internal Server Error — Catch-all
    // -------------------------------------------------------------------------

    /**
     * Catch-all handler for any unexpected exception not covered by a more specific handler.
     *
     * <p>Logs the full exception server-side but returns a generic message to the client
     * so that internal implementation details are never exposed.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGenericException(
            Exception ex,
            HttpServletRequest request) {

        log.error("Unhandled exception at {}: {}", request.getRequestURI(), ex.getMessage(), ex);

        ApiErrorResponse response = ApiErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .message("An unexpected error occurred. Please try again later.")
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
