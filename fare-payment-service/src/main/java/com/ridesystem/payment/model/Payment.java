package com.ridesystem.payment.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * MongoDB document representing a fare payment transaction.
 *
 * <p>Maps to the {@code payments} collection in the {@code fare_payment_db} database.
 *
 * <p><strong>Duplicate-payment prevention:</strong> {@code rideId} carries a unique index
 * ({@code @Indexed(unique = true)}). This ensures that at most one payment document can
 * exist per ride, allowing the service layer to enforce duplicate-payment prevention safely.
 *
 * <p><strong>Monetary values:</strong> All fare and amount fields use {@link BigDecimal}
 * to avoid floating-point rounding issues. {@code double} and {@code float} are intentionally
 * NOT used for any money-related field.
 *
 * <p><strong>Timestamps:</strong> {@code createdAt} and {@code updatedAt} are managed
 * automatically by Spring Data MongoDB Auditing ({@code @CreatedDate} /
 * {@code @LastModifiedDate}). {@code @EnableMongoAuditing} must be present on the
 * application or a configuration class (see {@code FarePaymentServiceApplication}).
 *
 * <p>IT3130 AD Group Assignment — RideLink System
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "payments")
public class Payment {

    // -----------------------------------------------------------------------
    // Identity
    // -----------------------------------------------------------------------

    /**
     * MongoDB document identifier (auto-generated ObjectId).
     */
    @Id
    private String id;

    /**
     * Reference to the ride this payment belongs to.
     *
     * <p>Carries a unique MongoDB index to prevent duplicate payments for the
     * same ride. The uniqueness constraint is enforced at the database level,
     * providing a hard guarantee in addition to any application-level checks.
     */
    @NotBlank(message = "Ride ID must not be blank")
    @Indexed(unique = true)
    @Field("rideId")
    private String rideId;

    /**
     * Reference to the passenger who made this payment.
     * Stored by ID only — no cross-service repository dependency.
     */
    @NotBlank(message = "Passenger ID must not be blank")
    @Indexed
    @Field("passengerId")
    private String passengerId;

    /**
     * Reference to the driver associated with this payment.
     * Stored by ID only — no cross-service repository dependency.
     */
    @NotBlank(message = "Driver ID must not be blank")
    @Indexed
    @Field("driverId")
    private String driverId;

    // -----------------------------------------------------------------------
    // Fare Breakdown  (all monetary values use BigDecimal)
    // -----------------------------------------------------------------------

    /**
     * Fixed base fare charged for every ride.
     */
    @NotNull(message = "Base fare must not be null")
    @DecimalMin(value = "0.00", message = "Base fare must be zero or positive")
    @Field("baseFare")
    private BigDecimal baseFare;

    /**
     * Variable fare calculated from the ride distance.
     */
    @NotNull(message = "Distance fare must not be null")
    @DecimalMin(value = "0.00", message = "Distance fare must be zero or positive")
    @Field("distanceFare")
    private BigDecimal distanceFare;

    /**
     * Variable fare calculated from the ride duration.
     */
    @NotNull(message = "Time fare must not be null")
    @DecimalMin(value = "0.00", message = "Time fare must be zero or positive")
    @Field("timeFare")
    private BigDecimal timeFare;

    /**
     * Total amount charged to the passenger (baseFare + distanceFare + timeFare).
     */
    @NotNull(message = "Total amount must not be null")
    @DecimalMin(value = "0.00", message = "Total amount must be zero or positive")
    @Field("totalAmount")
    private BigDecimal totalAmount;

    // -----------------------------------------------------------------------
    // Payment Classification
    // -----------------------------------------------------------------------

    /**
     * Method used to make the payment (CASH, CARD, or WALLET).
     */
    @NotNull(message = "Payment method must not be null")
    @Field("paymentMethod")
    private PaymentMethod paymentMethod;

    /**
     * Current lifecycle status of the payment.
     */
    @NotNull(message = "Payment status must not be null")
    @Builder.Default
    @Field("paymentStatus")
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;

    // -----------------------------------------------------------------------
    // Transaction Reference
    // -----------------------------------------------------------------------

    /**
     * Unique external transaction reference (e.g., gateway reference number).
     * May be null for CASH payments where no gateway is involved.
     */
    @Field("transactionReference")
    private String transactionReference;

    // -----------------------------------------------------------------------
    // Refund Information
    // -----------------------------------------------------------------------

    /**
     * Timestamp when the refund was processed. Null until a refund is issued.
     */
    @Field("refundedAt")
    private LocalDateTime refundedAt;

    /**
     * Amount refunded to the passenger. Null until a refund is issued.
     */
    @DecimalMin(value = "0.00", message = "Refund amount must be zero or positive")
    @Field("refundAmount")
    private BigDecimal refundAmount;

    // -----------------------------------------------------------------------
    // Audit Timestamps (managed by Spring Data MongoDB Auditing)
    // -----------------------------------------------------------------------

    /**
     * Timestamp when this document was first created.
     * Populated automatically by {@code @EnableMongoAuditing}.
     */
    @CreatedDate
    @Field("createdAt")
    private LocalDateTime createdAt;

    /**
     * Timestamp of the most recent update to this document.
     * Populated automatically by {@code @EnableMongoAuditing}.
     */
    @LastModifiedDate
    @Field("updatedAt")
    private LocalDateTime updatedAt;
}
