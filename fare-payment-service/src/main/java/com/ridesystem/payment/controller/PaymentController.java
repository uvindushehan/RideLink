package com.ridesystem.payment.controller;

import com.ridesystem.payment.dto.*;
import com.ridesystem.payment.model.PaymentStatus;
import com.ridesystem.payment.service.FareCalculationService;
import com.ridesystem.payment.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for the Fare Payment Service.
 *
 * <p>IT3130 AD Group Assignment — RideLink System
 */
@RestController
@RequestMapping("/api/payments")
@Tag(name = "Fare Payment API", description = "Endpoints for managing ride payments, calculating fares, and processing refunds.")
public class PaymentController {

    private final PaymentService paymentService;
    private final FareCalculationService fareCalculationService;

    public PaymentController(PaymentService paymentService, FareCalculationService fareCalculationService) {
        this.paymentService = paymentService;
        this.fareCalculationService = fareCalculationService;
    }

    @PostMapping
    @Operation(summary = "Create a payment", description = "Creates a new payment record for a ride.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Payment successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid request payload"),
            @ApiResponse(responseCode = "409", description = "Payment for this ride already exists")
    })
    public ResponseEntity<PaymentResponse> createPayment(@Valid @RequestBody CreatePaymentRequest request) {
        PaymentResponse response = paymentService.createPayment(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{paymentId}")
    @Operation(summary = "Get payment by ID", description = "Retrieves a specific payment by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment found"),
            @ApiResponse(responseCode = "404", description = "Payment not found")
    })
    public ResponseEntity<PaymentResponse> getPaymentById(@PathVariable String paymentId) {
        PaymentResponse response = paymentService.getPaymentById(paymentId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/ride/{rideId}")
    @Operation(summary = "Get payment by ride ID", description = "Retrieves the payment associated with a specific ride ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment found"),
            @ApiResponse(responseCode = "404", description = "Payment not found")
    })
    public ResponseEntity<PaymentResponse> getPaymentByRideId(@PathVariable String rideId) {
        PaymentResponse response = paymentService.getPaymentByRideId(rideId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/passenger/{passengerId}")
    @Operation(summary = "Get passenger payment history", description = "Retrieves all payment records for a specific passenger.")
    public ResponseEntity<List<PaymentResponse>> getPaymentsByPassengerId(@PathVariable String passengerId) {
        List<PaymentResponse> responses = paymentService.getPaymentsByPassengerId(passengerId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/driver/{driverId}")
    @Operation(summary = "Get driver payment records", description = "Retrieves all payment records associated with a specific driver.")
    public ResponseEntity<List<PaymentResponse>> getPaymentsByDriverId(@PathVariable String driverId) {
        List<PaymentResponse> responses = paymentService.getPaymentsByDriverId(driverId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get payments by status", description = "Retrieves all payments matching the specified status (PENDING, COMPLETED, FAILED, REFUNDED).")
    public ResponseEntity<List<PaymentResponse>> getPaymentsByStatus(@PathVariable PaymentStatus status) {
        List<PaymentResponse> responses = paymentService.getPaymentsByStatus(status);
        return ResponseEntity.ok(responses);
    }

    @PatchMapping("/{paymentId}/status")
    @Operation(summary = "Update payment status", description = "Updates the lifecycle status of an existing payment.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request payload"),
            @ApiResponse(responseCode = "404", description = "Payment not found")
    })
    public ResponseEntity<PaymentResponse> updatePaymentStatus(
            @PathVariable String paymentId,
            @Valid @RequestBody UpdatePaymentStatusRequest request) {
        PaymentResponse response = paymentService.updatePaymentStatus(paymentId, request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{paymentId}/refund")
    @Operation(summary = "Refund a payment", description = "Processes a refund for a completed payment.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Refund processed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid refund request (e.g. not COMPLETED, over-refund)"),
            @ApiResponse(responseCode = "404", description = "Payment not found")
    })
    public ResponseEntity<PaymentResponse> refundPayment(
            @PathVariable String paymentId,
            @Valid @RequestBody RefundRequest request) {
        PaymentResponse response = paymentService.refundPayment(paymentId, request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/calculate-fare")
    @Operation(summary = "Calculate fare", description = "Previews the fare breakdown for a ride without creating a payment record.")
    public ResponseEntity<FareCalculationResult> calculateFare(@Valid @RequestBody CalculateFareRequest request) {
        FareCalculationResult result = fareCalculationService.calculate(
                request.getDistanceKm(), 
                request.getDurationMinutes());
        return ResponseEntity.ok(result);
    }
}
