package com.ridesystem.payment.model;

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
 * exist per ride.
 *
 * <p><strong>Monetary values:</strong> All fare and amount fields use {@link BigDecimal}.
 * {@code double} and {@code float} are intentionally NOT used for any money-related field.
 *
 * <p><strong>Timestamps:</strong> {@code createdAt} and {@code updatedAt} are managed
 * automatically by Spring Data MongoDB Auditing ({@code @CreatedDate} /
 * {@code @LastModifiedDate}).
 *
 * <p>Note: Lombok annotations removed due to Java 26 / Lombok annotation-processor
 * incompatibility (TypeTag::UNKNOWN). Getters, setters, and builder are written explicitly.
 *
 */
@Document(collection = "payments")
public class Payment {

    // -----------------------------------------------------------------------
    // Identity
    // -----------------------------------------------------------------------

    @Id
    private String id;

    @NotBlank(message = "Ride ID must not be blank")
    @Indexed(unique = true)
    @Field("rideId")
    private String rideId;

    @NotBlank(message = "Passenger ID must not be blank")
    @Indexed
    @Field("passengerId")
    private String passengerId;

    @NotBlank(message = "Driver ID must not be blank")
    @Indexed
    @Field("driverId")
    private String driverId;

    // -----------------------------------------------------------------------
    // Fare Breakdown (all monetary values use BigDecimal)
    // -----------------------------------------------------------------------

    @NotNull(message = "Base fare must not be null")
    @DecimalMin(value = "0.00", message = "Base fare must be zero or positive")
    @Field("baseFare")
    private BigDecimal baseFare;

    @NotNull(message = "Distance fare must not be null")
    @DecimalMin(value = "0.00", message = "Distance fare must be zero or positive")
    @Field("distanceFare")
    private BigDecimal distanceFare;

    @NotNull(message = "Time fare must not be null")
    @DecimalMin(value = "0.00", message = "Time fare must be zero or positive")
    @Field("timeFare")
    private BigDecimal timeFare;

    @NotNull(message = "Total amount must not be null")
    @DecimalMin(value = "0.00", message = "Total amount must be zero or positive")
    @Field("totalAmount")
    private BigDecimal totalAmount;

    // -----------------------------------------------------------------------
    // Payment Classification
    // -----------------------------------------------------------------------

    @NotNull(message = "Payment method must not be null")
    @Field("paymentMethod")
    private PaymentMethod paymentMethod;

    @NotNull(message = "Payment status must not be null")
    @Field("paymentStatus")
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;

    // -----------------------------------------------------------------------
    // Transaction Reference
    // -----------------------------------------------------------------------

    @Field("transactionReference")
    private String transactionReference;

    // -----------------------------------------------------------------------
    // Refund Information
    // -----------------------------------------------------------------------

    @Field("refundedAt")
    private LocalDateTime refundedAt;

    @DecimalMin(value = "0.00", message = "Refund amount must be zero or positive")
    @Field("refundAmount")
    private BigDecimal refundAmount;

    // -----------------------------------------------------------------------
    // Audit Timestamps (managed by Spring Data MongoDB Auditing)
    // -----------------------------------------------------------------------

    @CreatedDate
    @Field("createdAt")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Field("updatedAt")
    private LocalDateTime updatedAt;

    // -----------------------------------------------------------------------
    // Constructors
    // -----------------------------------------------------------------------

    public Payment() {}

    // -----------------------------------------------------------------------
    // Builder
    // -----------------------------------------------------------------------

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String        id;
        private String        rideId;
        private String        passengerId;
        private String        driverId;
        private BigDecimal    baseFare;
        private BigDecimal    distanceFare;
        private BigDecimal    timeFare;
        private BigDecimal    totalAmount;
        private PaymentMethod paymentMethod;
        private PaymentStatus paymentStatus = PaymentStatus.PENDING;
        private String        transactionReference;
        private LocalDateTime refundedAt;
        private BigDecimal    refundAmount;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public Builder id(String v)                          { this.id = v; return this; }
        public Builder rideId(String v)                      { this.rideId = v; return this; }
        public Builder passengerId(String v)                 { this.passengerId = v; return this; }
        public Builder driverId(String v)                    { this.driverId = v; return this; }
        public Builder baseFare(BigDecimal v)                { this.baseFare = v; return this; }
        public Builder distanceFare(BigDecimal v)            { this.distanceFare = v; return this; }
        public Builder timeFare(BigDecimal v)                { this.timeFare = v; return this; }
        public Builder totalAmount(BigDecimal v)             { this.totalAmount = v; return this; }
        public Builder paymentMethod(PaymentMethod v)        { this.paymentMethod = v; return this; }
        public Builder paymentStatus(PaymentStatus v)        { this.paymentStatus = v; return this; }
        public Builder transactionReference(String v)        { this.transactionReference = v; return this; }
        public Builder refundedAt(LocalDateTime v)           { this.refundedAt = v; return this; }
        public Builder refundAmount(BigDecimal v)            { this.refundAmount = v; return this; }
        public Builder createdAt(LocalDateTime v)            { this.createdAt = v; return this; }
        public Builder updatedAt(LocalDateTime v)            { this.updatedAt = v; return this; }

        public Payment build() {
            Payment p = new Payment();
            p.id                   = this.id;
            p.rideId               = this.rideId;
            p.passengerId          = this.passengerId;
            p.driverId             = this.driverId;
            p.baseFare             = this.baseFare;
            p.distanceFare         = this.distanceFare;
            p.timeFare             = this.timeFare;
            p.totalAmount          = this.totalAmount;
            p.paymentMethod        = this.paymentMethod;
            p.paymentStatus        = this.paymentStatus;
            p.transactionReference = this.transactionReference;
            p.refundedAt           = this.refundedAt;
            p.refundAmount         = this.refundAmount;
            p.createdAt            = this.createdAt;
            p.updatedAt            = this.updatedAt;
            return p;
        }
    }

    // -----------------------------------------------------------------------
    // Getters and Setters
    // -----------------------------------------------------------------------

    public String getId()                          { return id; }
    public void   setId(String v)                  { this.id = v; }

    public String getRideId()                      { return rideId; }
    public void   setRideId(String v)              { this.rideId = v; }

    public String getPassengerId()                 { return passengerId; }
    public void   setPassengerId(String v)         { this.passengerId = v; }

    public String getDriverId()                    { return driverId; }
    public void   setDriverId(String v)            { this.driverId = v; }

    public BigDecimal getBaseFare()                { return baseFare; }
    public void       setBaseFare(BigDecimal v)    { this.baseFare = v; }

    public BigDecimal getDistanceFare()              { return distanceFare; }
    public void       setDistanceFare(BigDecimal v)  { this.distanceFare = v; }

    public BigDecimal getTimeFare()                { return timeFare; }
    public void       setTimeFare(BigDecimal v)    { this.timeFare = v; }

    public BigDecimal getTotalAmount()               { return totalAmount; }
    public void       setTotalAmount(BigDecimal v)   { this.totalAmount = v; }

    public PaymentMethod getPaymentMethod()                { return paymentMethod; }
    public void          setPaymentMethod(PaymentMethod v) { this.paymentMethod = v; }

    public PaymentStatus getPaymentStatus()                { return paymentStatus; }
    public void          setPaymentStatus(PaymentStatus v) { this.paymentStatus = v; }

    public String getTransactionReference()              { return transactionReference; }
    public void   setTransactionReference(String v)      { this.transactionReference = v; }

    public LocalDateTime getRefundedAt()                 { return refundedAt; }
    public void          setRefundedAt(LocalDateTime v)  { this.refundedAt = v; }

    public BigDecimal getRefundAmount()                  { return refundAmount; }
    public void       setRefundAmount(BigDecimal v)      { this.refundAmount = v; }

    public LocalDateTime getCreatedAt()                  { return createdAt; }
    public void          setCreatedAt(LocalDateTime v)   { this.createdAt = v; }

    public LocalDateTime getUpdatedAt()                  { return updatedAt; }
    public void          setUpdatedAt(LocalDateTime v)   { this.updatedAt = v; }
}
