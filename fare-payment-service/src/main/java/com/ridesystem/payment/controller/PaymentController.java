package com.ridesystem.payment.controller;

import com.ridesystem.payment.dto.*;
import com.ridesystem.payment.model.PaymentStatus;
import com.ridesystem.payment.service.FareCalculationService;
import com.ridesystem.payment.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for the Fare Payment Service.
 *
 */
@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;
    private final FareCalculationService fareCalculationService;

    public PaymentController(PaymentService paymentService, FareCalculationService fareCalculationService) {
        this.paymentService = paymentService;
        this.fareCalculationService = fareCalculationService;
    }

    @PostMapping
            public ResponseEntity<PaymentResponse> createPayment(@Valid @RequestBody CreatePaymentRequest request) {
        PaymentResponse response = paymentService.createPayment(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{paymentId}")
            public ResponseEntity<PaymentResponse> getPaymentById(@PathVariable String paymentId) {
        PaymentResponse response = paymentService.getPaymentById(paymentId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/ride/{rideId}")
            public ResponseEntity<PaymentResponse> getPaymentByRideId(@PathVariable String rideId) {
        PaymentResponse response = paymentService.getPaymentByRideId(rideId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/passenger/{passengerId}")
        public ResponseEntity<List<PaymentResponse>> getPaymentsByPassengerId(@PathVariable String passengerId) {
        List<PaymentResponse> responses = paymentService.getPaymentsByPassengerId(passengerId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/driver/{driverId}")
        public ResponseEntity<List<PaymentResponse>> getPaymentsByDriverId(@PathVariable String driverId) {
        List<PaymentResponse> responses = paymentService.getPaymentsByDriverId(driverId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/status/{status}")
        public ResponseEntity<List<PaymentResponse>> getPaymentsByStatus(@PathVariable PaymentStatus status) {
        List<PaymentResponse> responses = paymentService.getPaymentsByStatus(status);
        return ResponseEntity.ok(responses);
    }

    @PatchMapping("/{paymentId}/status")
            public ResponseEntity<PaymentResponse> updatePaymentStatus(
            @PathVariable String paymentId,
            @Valid @RequestBody UpdatePaymentStatusRequest request) {
        PaymentResponse response = paymentService.updatePaymentStatus(paymentId, request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{paymentId}/refund")
            public ResponseEntity<PaymentResponse> refundPayment(
            @PathVariable String paymentId,
            @Valid @RequestBody RefundRequest request) {
        PaymentResponse response = paymentService.refundPayment(paymentId, request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/calculate-fare")
        public ResponseEntity<FareCalculationResult> calculateFare(@Valid @RequestBody CalculateFareRequest request) {
        FareCalculationResult result = fareCalculationService.calculate(
                request.getDistanceKm(), 
                request.getDurationMinutes());
        return ResponseEntity.ok(result);
    }
}
