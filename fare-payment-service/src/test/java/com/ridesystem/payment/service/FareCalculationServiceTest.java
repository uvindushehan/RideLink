package com.ridesystem.payment.service;

import com.ridesystem.payment.dto.FareCalculationResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class FareCalculationServiceTest {

    private FareCalculationService fareCalculationService;

    @BeforeEach
    void setUp() {
        fareCalculationService = new FareCalculationService();
    }

    @Test
    void testCalculateFare_Normal() {
        BigDecimal distance = new BigDecimal("10");
        BigDecimal duration = new BigDecimal("20");

        FareCalculationResult result = fareCalculationService.calculate(distance, duration);

        assertNotNull(result);
        assertEquals(new BigDecimal("200.00"), result.getBaseFare());
        assertEquals(new BigDecimal("800.00"), result.getDistanceFare());
        assertEquals(new BigDecimal("100.00"), result.getTimeFare());
        assertEquals(new BigDecimal("1100.00"), result.getTotalAmount());
    }

    @Test
    void testCalculateFare_ZeroDistance() {
        BigDecimal distance = new BigDecimal("0");
        BigDecimal duration = new BigDecimal("20");

        FareCalculationResult result = fareCalculationService.calculate(distance, duration);

        assertEquals(new BigDecimal("200.00"), result.getBaseFare());
        assertEquals(new BigDecimal("0.00"), result.getDistanceFare());
        assertEquals(new BigDecimal("100.00"), result.getTimeFare());
        assertEquals(new BigDecimal("300.00"), result.getTotalAmount());
    }

    @Test
    void testCalculateFare_ZeroDuration() {
        BigDecimal distance = new BigDecimal("10");
        BigDecimal duration = new BigDecimal("0");

        FareCalculationResult result = fareCalculationService.calculate(distance, duration);

        assertEquals(new BigDecimal("200.00"), result.getBaseFare());
        assertEquals(new BigDecimal("800.00"), result.getDistanceFare());
        assertEquals(new BigDecimal("0.00"), result.getTimeFare());
        assertEquals(new BigDecimal("1000.00"), result.getTotalAmount());
    }

    @Test
    void testCalculateFare_DecimalValues() {
        BigDecimal distance = new BigDecimal("12.5");
        BigDecimal duration = new BigDecimal("15.5");

        FareCalculationResult result = fareCalculationService.calculate(distance, duration);

        // distance = 12.5 * 80 = 1000.00
        // duration = 15.5 * 5 = 77.50
        // total = 200 + 1000 + 77.50 = 1277.50
        assertEquals(new BigDecimal("200.00"), result.getBaseFare());
        assertEquals(new BigDecimal("1000.00"), result.getDistanceFare());
        assertEquals(new BigDecimal("77.50"), result.getTimeFare());
        assertEquals(new BigDecimal("1277.50"), result.getTotalAmount());
    }
}
