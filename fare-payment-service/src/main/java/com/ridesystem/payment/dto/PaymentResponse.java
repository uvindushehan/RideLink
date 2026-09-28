package com.ridesystem.payment.dto;

import com.ridesystem.payment.model.PaymentMethod;
import com.ridesystem.payment.model.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Response DTO returned to API clients for all payment operations.
 *
 * <p>IT3130 AD Group Assignment — RideLink System
 */
public class PaymentResponse {

    private String          id;
    private String          rideId;
    private String          passengerId;
    private String          driverId;
    private BigDecimal      baseFare;
    private BigDecimal      distanceFare;
    private BigDecimal      timeFare;
    private BigDecimal      totalAmount;
    private PaymentMethod   paymentMethod;
    private PaymentStatus   paymentStatus;
    private String          transactionReference;
    private LocalDateTime   refundedAt;
    private BigDecimal      refundAmount;
    private LocalDateTime   createdAt;
    private LocalDateTime   updatedAt;

    // -----------------------------------------------------------------------
    // Constructors
    // -----------------------------------------------------------------------

    public PaymentResponse() {}

    // -----------------------------------------------------------------------
    // Builder
    // -----------------------------------------------------------------------

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String          id;
        private String          rideId;
        private String          passengerId;
        private String          driverId;
        private BigDecimal      baseFare;
        private BigDecimal      distanceFare;
        private BigDecimal      timeFare;
        private BigDecimal      totalAmount;
        private PaymentMethod   paymentMethod;
        private PaymentStatus   paymentStatus;
        private String          transactionReference;
        private LocalDateTime   refundedAt;
        private BigDecimal      refundAmount;
        private LocalDateTime   createdAt;
        private LocalDateTime   updatedAt;

        public Builder id(String v)                        { this.id = v; return this; }
        public Builder rideId(String v)                    { this.rideId = v; return this; }
        public Builder passengerId(String v)               { this.passengerId = v; return this; }
        public Builder driverId(String v)                  { this.driverId = v; return this; }
        public Builder baseFare(BigDecimal v)              { this.baseFare = v; return this; }
        public Builder distanceFare(BigDecimal v)          { this.distanceFare = v; return this; }
        public Builder timeFare(BigDecimal v)              { this.timeFare = v; return this; }
        public Builder totalAmount(BigDecimal v)           { this.totalAmount = v; return this; }
        public Builder paymentMethod(PaymentMethod v)      { this.paymentMethod = v; return this; }
        public Builder paymentStatus(PaymentStatus v)      { this.paymentStatus = v; return this; }
        public Builder transactionReference(String v)      { this.transactionReference = v; return this; }
        public Builder refundedAt(LocalDateTime v)         { this.refundedAt = v; return this; }
        public Builder refundAmount(BigDecimal v)          { this.refundAmount = v; return this; }
        public Builder createdAt(LocalDateTime v)          { this.createdAt = v; return this; }
        public Builder updatedAt(LocalDateTime v)          { this.updatedAt = v; return this; }

        public PaymentResponse build() {
            PaymentResponse r = new PaymentResponse();
            r.id                   = this.id;
            r.rideId               = this.rideId;
            r.passengerId          = this.passengerId;
            r.driverId             = this.driverId;
            r.baseFare             = this.baseFare;
            r.distanceFare         = this.distanceFare;
            r.timeFare             = this.timeFare;
            r.totalAmount          = this.totalAmount;
            r.paymentMethod        = this.paymentMethod;
            r.paymentStatus        = this.paymentStatus;
            r.transactionReference = this.transactionReference;
            r.refundedAt           = this.refundedAt;
            r.refundAmount         = this.refundAmount;
            r.createdAt            = this.createdAt;
            r.updatedAt            = this.updatedAt;
            return r;
        }
    }

    // -----------------------------------------------------------------------
    // Getters and Setters
    // -----------------------------------------------------------------------

    public String getId()                              { return id; }
    public void   setId(String v)                     { this.id = v; }

    public String getRideId()                         { return rideId; }
    public void   setRideId(String v)                 { this.rideId = v; }

    public String getPassengerId()                    { return passengerId; }
    public void   setPassengerId(String v)            { this.passengerId = v; }

    public String getDriverId()                       { return driverId; }
    public void   setDriverId(String v)               { this.driverId = v; }

    public BigDecimal getBaseFare()                   { return baseFare; }
    public void       setBaseFare(BigDecimal v)       { this.baseFare = v; }

    public BigDecimal getDistanceFare()               { return distanceFare; }
    public void       setDistanceFare(BigDecimal v)   { this.distanceFare = v; }

    public BigDecimal getTimeFare()                   { return timeFare; }
    public void       setTimeFare(BigDecimal v)       { this.timeFare = v; }

    public BigDecimal getTotalAmount()                { return totalAmount; }
    public void       setTotalAmount(BigDecimal v)    { this.totalAmount = v; }

    public PaymentMethod getPaymentMethod()                  { return paymentMethod; }
    public void          setPaymentMethod(PaymentMethod v)   { this.paymentMethod = v; }

    public PaymentStatus getPaymentStatus()                  { return paymentStatus; }
    public void          setPaymentStatus(PaymentStatus v)   { this.paymentStatus = v; }

    public String getTransactionReference()                  { return transactionReference; }
    public void   setTransactionReference(String v)          { this.transactionReference = v; }

    public LocalDateTime getRefundedAt()                     { return refundedAt; }
    public void          setRefundedAt(LocalDateTime v)      { this.refundedAt = v; }

    public BigDecimal getRefundAmount()                      { return refundAmount; }
    public void       setRefundAmount(BigDecimal v)          { this.refundAmount = v; }

    public LocalDateTime getCreatedAt()                      { return createdAt; }
    public void          setCreatedAt(LocalDateTime v)       { this.createdAt = v; }

    public LocalDateTime getUpdatedAt()                      { return updatedAt; }
    public void          setUpdatedAt(LocalDateTime v)       { this.updatedAt = v; }
}
