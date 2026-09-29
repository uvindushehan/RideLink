package com.ridesystem.payment.service;

import com.ridesystem.payment.dto.*;
import com.ridesystem.payment.exception.DuplicatePaymentException;
import com.ridesystem.payment.exception.InvalidPaymentOperationException;
import com.ridesystem.payment.exception.PaymentNotFoundException;
import com.ridesystem.payment.model.Payment;
import com.ridesystem.payment.model.PaymentStatus;
import com.ridesystem.payment.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Core business-logic service for the Fare Payment Service.
 *
 * <p>Responsibilities:
 * <ul>
 *     <li>Create new payment records with server-side fare calculation</li>
 *     <li>Prevent duplicate payments per ride using {@code PaymentRepository.existsByRideId()}</li>
 *     <li>Retrieve payment records by various criteria</li>
 *     <li>Update payment status through the payment lifecycle</li>
 *     <li>Process refunds with full validation</li>
 * </ul>
 *
 * <p><strong>Architecture rule:</strong> This service only stores and retrieves IDs
 * (rideId, passengerId, driverId). It does NOT access databases belonging to
 * Ride Management, Account, or Driver services.
 *
 * <p><strong>Injection:</strong> Dependencies are injected via constructor (not field
 * injection) to support testability and make dependencies explicit.
 *
 */
@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final FareCalculationService fareCalculationService;

    // -----------------------------------------------------------------------
    // Constructor Injection
    // -----------------------------------------------------------------------

    public PaymentService(PaymentRepository paymentRepository,
                          FareCalculationService fareCalculationService) {
        this.paymentRepository     = paymentRepository;
        this.fareCalculationService = fareCalculationService;
    }

    // -----------------------------------------------------------------------
    // Create Payment
    // -----------------------------------------------------------------------

    /**
     * Create a new payment for a ride.
     *
     * <p>Steps:
     * <ol>
     *     <li>Check for an existing payment for the same ride — reject if found.</li>
     *     <li>Calculate fare using {@link FareCalculationService}.</li>
     *     <li>Generate a unique transaction reference (TXN-{UUID}).</li>
     *     <li>Persist the {@link Payment} document with status {@code PENDING}.</li>
     *     <li>Return a {@link PaymentResponse}.</li>
     * </ol>
     *
     * @param request the payment creation request
     * @return the created payment as a {@link PaymentResponse}
     * @throws DuplicatePaymentException if a payment already exists for the ride
     */
    public PaymentResponse createPayment(CreatePaymentRequest request) {

        // Step 1 — Duplicate-payment prevention (application-level guard)
        if (paymentRepository.existsByRideId(request.getRideId())) {
            throw DuplicatePaymentException.forRide(request.getRideId());
        }

        // Step 2 — Server-side fare calculation (BigDecimal, no double/float)
        FareCalculationResult fare = fareCalculationService.calculate(
                request.getDistanceKm(),
                request.getDurationMinutes()
        );

        // Step 3 — Generate a human-readable transaction reference
        String transactionReference = generateTransactionReference();

        // Step 4 — Build and persist the Payment document
        Payment payment = Payment.builder()
                .rideId(request.getRideId())
                .passengerId(request.getPassengerId())
                .driverId(request.getDriverId())
                .baseFare(fare.getBaseFare())
                .distanceFare(fare.getDistanceFare())
                .timeFare(fare.getTimeFare())
                .totalAmount(fare.getTotalAmount())
                .paymentMethod(request.getPaymentMethod())
                .paymentStatus(PaymentStatus.PENDING)
                .transactionReference(transactionReference)
                .build();

        Payment savedPayment = paymentRepository.save(payment);

        // Step 5 — Map and return
        return mapToResponse(savedPayment);
    }

    // -----------------------------------------------------------------------
    // Lookup Methods
    // -----------------------------------------------------------------------

    /**
     * Retrieve a payment by its MongoDB document ID.
     *
     * @param paymentId the payment identifier
     * @return the matching payment as a {@link PaymentResponse}
     * @throws PaymentNotFoundException if no payment with this ID exists
     */
    public PaymentResponse getPaymentById(String paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> PaymentNotFoundException.byId(paymentId));
        return mapToResponse(payment);
    }

    /**
     * Retrieve a payment by the ride it was made for.
     *
     * @param rideId the ride identifier
     * @return the matching payment as a {@link PaymentResponse}
     * @throws PaymentNotFoundException if no payment exists for this ride
     */
    public PaymentResponse getPaymentByRideId(String rideId) {
        Payment payment = paymentRepository.findByRideId(rideId)
                .orElseThrow(() -> PaymentNotFoundException.byRideId(rideId));
        return mapToResponse(payment);
    }

    /**
     * Retrieve the complete payment history for a passenger.
     *
     * @param passengerId the passenger identifier
     * @return list of payments (may be empty if the passenger has no payments)
     */
    public List<PaymentResponse> getPaymentsByPassengerId(String passengerId) {
        return paymentRepository.findByPassengerId(passengerId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Retrieve all payment records associated with a driver.
     *
     * @param driverId the driver identifier
     * @return list of payments (may be empty)
     */
    public List<PaymentResponse> getPaymentsByDriverId(String driverId) {
        return paymentRepository.findByDriverId(driverId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Retrieve all payments matching a given lifecycle status.
     *
     * @param status the payment status to filter by
     * @return list of payments in the given status (may be empty)
     */
    public List<PaymentResponse> getPaymentsByStatus(PaymentStatus status) {
        return paymentRepository.findByPaymentStatus(status)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // -----------------------------------------------------------------------
    // Status Update
    // -----------------------------------------------------------------------

    /**
     * Update the status of an existing payment.
     *
     * <p>Updates {@code paymentStatus}. The {@code updatedAt} timestamp
     * is managed automatically by {@code @LastModifiedDate} / MongoAuditing.
     *
     * @param paymentId the payment to update
     * @param request   the new status to apply
     * @return the updated payment as a {@link PaymentResponse}
     * @throws PaymentNotFoundException if no payment with this ID exists
     */
    public PaymentResponse updatePaymentStatus(String paymentId,
                                               UpdatePaymentStatusRequest request) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> PaymentNotFoundException.byId(paymentId));

        payment.setPaymentStatus(request.getPaymentStatus());

        Payment updated = paymentRepository.save(payment);
        return mapToResponse(updated);
    }

    // -----------------------------------------------------------------------
    // Refund
    // -----------------------------------------------------------------------

    /**
     * Process a refund for a completed payment.
     *
     * <p>Validation rules (all enforced before any state change):
     * <ol>
     *     <li>Payment must exist.</li>
     *     <li>Payment must be in {@code COMPLETED} status — not PENDING, FAILED, or REFUNDED.</li>
     *     <li>Payment must not already be {@code REFUNDED}.</li>
     *     <li>Refund amount must not exceed the original {@code totalAmount}.</li>
     * </ol>
     *
     * <p>On success:
     * <ul>
     *     <li>{@code paymentStatus} → {@code REFUNDED}</li>
     *     <li>{@code refundAmount} is set</li>
     *     <li>{@code refundedAt} is set to {@code LocalDateTime.now()}</li>
     * </ul>
     *
     * @param paymentId the payment to refund
     * @param request   the refund request containing the refund amount
     * @return the updated payment as a {@link PaymentResponse}
     * @throws PaymentNotFoundException         if no payment with this ID exists
     * @throws InvalidPaymentOperationException if any refund validation rule is violated
     */
    public PaymentResponse refundPayment(String paymentId, RefundRequest request) {

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> PaymentNotFoundException.byId(paymentId));

        // Rule 1 — Only COMPLETED payments can be refunded
        if (payment.getPaymentStatus() == PaymentStatus.REFUNDED) {
            throw InvalidPaymentOperationException.alreadyRefunded();
        }
        if (payment.getPaymentStatus() != PaymentStatus.COMPLETED) {
            throw InvalidPaymentOperationException.refundNotAllowed(
                    payment.getPaymentStatus().name()
            );
        }

        // Rule 2 — Refund amount must not exceed total amount
        if (request.getRefundAmount().compareTo(payment.getTotalAmount()) > 0) {
            throw InvalidPaymentOperationException.refundExceedsTotal();
        }

        // Apply refund
        payment.setPaymentStatus(PaymentStatus.REFUNDED);
        payment.setRefundAmount(request.getRefundAmount());
        payment.setRefundedAt(LocalDateTime.now());

        Payment updated = paymentRepository.save(payment);
        return mapToResponse(updated);
    }

    // -----------------------------------------------------------------------
    // Private Helpers
    // -----------------------------------------------------------------------

    /**
     * Generate a unique, human-readable transaction reference.
     *
     * <p>Format: {@code TXN-<UPPERCASE_UUID>}
     * Example: {@code TXN-3F6A1C2B-4E7D-4A9F-BB5C-123456789ABC}
     */
    private String generateTransactionReference() {
        return "TXN-" + UUID.randomUUID().toString().toUpperCase();
    }

    /**
     * Map a {@link Payment} MongoDB document to a {@link PaymentResponse} DTO.
     *
     * <p>Centralised in a single private method to avoid duplicating mapping
     * logic across the multiple service methods that return {@link PaymentResponse}.
     *
     * @param payment the source MongoDB document
     * @return the mapped response DTO
     */
    private PaymentResponse mapToResponse(Payment payment) {
        return PaymentResponse.builder()
                .id(payment.getId())
                .rideId(payment.getRideId())
                .passengerId(payment.getPassengerId())
                .driverId(payment.getDriverId())
                .baseFare(payment.getBaseFare())
                .distanceFare(payment.getDistanceFare())
                .timeFare(payment.getTimeFare())
                .totalAmount(payment.getTotalAmount())
                .paymentMethod(payment.getPaymentMethod())
                .paymentStatus(payment.getPaymentStatus())
                .transactionReference(payment.getTransactionReference())
                .refundedAt(payment.getRefundedAt())
                .refundAmount(payment.getRefundAmount())
                .createdAt(payment.getCreatedAt())
                .updatedAt(payment.getUpdatedAt())
                .build();
    }
}
