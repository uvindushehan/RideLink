package com.ridesystem.payment.service;

import com.ridesystem.payment.dto.FareCalculationResult;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Service responsible for calculating ride fares.
 *
 * <p><strong>Formula:</strong>
 * <pre>
 *   distanceFare = distanceKm    × RATE_PER_KM        (LKR 80.00 / km)
 *   timeFare     = durationMins  × RATE_PER_MINUTE    (LKR  5.00 / min)
 *   totalAmount  = BASE_FARE + distanceFare + timeFare (BASE = LKR 200.00)
 * </pre>
 *
 * <p><strong>Precision:</strong> All arithmetic uses {@link BigDecimal} with
 * 2 decimal places and {@link RoundingMode#HALF_UP}. {@code double} and
 * {@code float} are never used for any monetary calculation.
 *
 * <p><strong>Constants:</strong> All rate values are centralised here as
 * named constants so they never need to be duplicated across the codebase.
 *
 */
@Service
public class FareCalculationService {

    // -----------------------------------------------------------------------
    // Fare Rate Constants  (LKR, scale 2)
    // -----------------------------------------------------------------------

    /** Fixed base fare charged for every ride, regardless of distance or time. */
    public static final BigDecimal BASE_FARE = new BigDecimal("200.00");

    /** Fare rate charged per kilometre of distance. */
    public static final BigDecimal RATE_PER_KM = new BigDecimal("80.00");

    /** Fare rate charged per minute of ride duration. */
    public static final BigDecimal RATE_PER_MINUTE = new BigDecimal("5.00");

    /** Scale (decimal places) applied to all monetary results. */
    private static final int MONETARY_SCALE = 2;

    /** Rounding mode applied to all monetary results. */
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;

    // -----------------------------------------------------------------------
    // Public API
    // -----------------------------------------------------------------------

    /**
     * Calculate the complete fare breakdown for a ride.
     *
     * @param distanceKm      ride distance in kilometres (must be ≥ 0)
     * @param durationMinutes ride duration in minutes (must be ≥ 0)
     * @return a {@link FareCalculationResult} containing all four monetary components
     */
    public FareCalculationResult calculate(BigDecimal distanceKm, BigDecimal durationMinutes) {

        BigDecimal distanceFare = calculateDistanceFare(distanceKm);
        BigDecimal timeFare     = calculateTimeFare(durationMinutes);
        BigDecimal totalAmount  = calculateTotal(distanceFare, timeFare);

        return FareCalculationResult.builder()
                .baseFare(BASE_FARE)
                .distanceFare(distanceFare)
                .timeFare(timeFare)
                .totalAmount(totalAmount)
                .build();
    }

    // -----------------------------------------------------------------------
    // Private Helpers
    // -----------------------------------------------------------------------

    /**
     * distanceFare = distanceKm × RATE_PER_KM
     */
    private BigDecimal calculateDistanceFare(BigDecimal distanceKm) {
        return distanceKm
                .multiply(RATE_PER_KM)
                .setScale(MONETARY_SCALE, ROUNDING_MODE);
    }

    /**
     * timeFare = durationMinutes × RATE_PER_MINUTE
     */
    private BigDecimal calculateTimeFare(BigDecimal durationMinutes) {
        return durationMinutes
                .multiply(RATE_PER_MINUTE)
                .setScale(MONETARY_SCALE, ROUNDING_MODE);
    }

    /**
     * totalAmount = BASE_FARE + distanceFare + timeFare
     */
    private BigDecimal calculateTotal(BigDecimal distanceFare, BigDecimal timeFare) {
        return BASE_FARE
                .add(distanceFare)
                .add(timeFare)
                .setScale(MONETARY_SCALE, ROUNDING_MODE);
    }
}
