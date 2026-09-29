package com.ridelink.ride.validation;

import com.ridelink.ride.enums.RideStatus;
import com.ridelink.ride.exception.InvalidRideStatusException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RideStatusValidatorTest {

    private RideStatusValidator validator;

    @BeforeEach
    void setUp() {
        validator = new RideStatusValidator();
    }

    @ParameterizedTest
    @CsvSource({
            "REQUESTED, ASSIGNED",
            "REQUESTED, CANCELLED",
            "ASSIGNED, ACCEPTED",
            "ASSIGNED, CANCELLED",
            "ACCEPTED, IN_PROGRESS",
            "ACCEPTED, CANCELLED",
            "IN_PROGRESS, COMPLETED"
    })
    void validateTransition_shouldAllowValidTransitions(RideStatus currentStatus, RideStatus targetStatus) {
        assertDoesNotThrow(() -> validator.validateTransition(currentStatus, targetStatus));
    }

    @ParameterizedTest
    @CsvSource({
            "REQUESTED, COMPLETED",
            "ASSIGNED, IN_PROGRESS",
            "IN_PROGRESS, CANCELLED",
            "COMPLETED, ASSIGNED",
            "CANCELLED, REQUESTED",
            "REQUESTED, REQUESTED"
    })
    void validateTransition_shouldRejectInvalidTransitions(RideStatus currentStatus, RideStatus targetStatus) {
        assertThrows(InvalidRideStatusException.class, 
                () -> validator.validateTransition(currentStatus, targetStatus));
    }
}
