package com.ridesystem.payment.dto;

import java.math.BigDecimal;

/**
 * Internal DTO carrying the results of a fare calculation.
 *
 * <p>Returned by {@code FareCalculationService} and consumed by
 * {@code PaymentService} when constructing a new {@code Payment} document.
 * Not exposed to API clients directly.
 *
 * <p>Formula:
 * <pre>
 *   distanceFare = distanceKm    × RATE_PER_KM      (LKR 80.00 / km)
 *   timeFare     = durationMins  × RATE_PER_MINUTE  (LKR  5.00 / min)
 *   totalAmount  = BASE_FARE + distanceFare + timeFare  (BASE = LKR 200.00)
 * </pre>
 *
 * <p>All values use {@link BigDecimal} with scale 2 and {@code HALF_UP} rounding.
 *
 * <p>IT3130 AD Group Assignment — RideLink System
 */
public class FareCalculationResult {

    private BigDecimal baseFare;
    private BigDecimal distanceFare;
    private BigDecimal timeFare;
    private BigDecimal totalAmount;

    // -----------------------------------------------------------------------
    // Constructors
    // -----------------------------------------------------------------------

    public FareCalculationResult() {}

    public FareCalculationResult(BigDecimal baseFare, BigDecimal distanceFare,
                                 BigDecimal timeFare, BigDecimal totalAmount) {
        this.baseFare     = baseFare;
        this.distanceFare = distanceFare;
        this.timeFare     = timeFare;
        this.totalAmount  = totalAmount;
    }

    // -----------------------------------------------------------------------
    // Builder
    // -----------------------------------------------------------------------

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private BigDecimal baseFare;
        private BigDecimal distanceFare;
        private BigDecimal timeFare;
        private BigDecimal totalAmount;

        public Builder baseFare(BigDecimal v)     { this.baseFare     = v; return this; }
        public Builder distanceFare(BigDecimal v) { this.distanceFare = v; return this; }
        public Builder timeFare(BigDecimal v)     { this.timeFare     = v; return this; }
        public Builder totalAmount(BigDecimal v)  { this.totalAmount  = v; return this; }

        public FareCalculationResult build() {
            return new FareCalculationResult(baseFare, distanceFare, timeFare, totalAmount);
        }
    }

    // -----------------------------------------------------------------------
    // Getters and Setters
    // -----------------------------------------------------------------------

    public BigDecimal getBaseFare()               { return baseFare; }
    public void       setBaseFare(BigDecimal v)   { this.baseFare = v; }

    public BigDecimal getDistanceFare()             { return distanceFare; }
    public void       setDistanceFare(BigDecimal v) { this.distanceFare = v; }

    public BigDecimal getTimeFare()               { return timeFare; }
    public void       setTimeFare(BigDecimal v)   { this.timeFare = v; }

    public BigDecimal getTotalAmount()              { return totalAmount; }
    public void       setTotalAmount(BigDecimal v)  { this.totalAmount = v; }
}
