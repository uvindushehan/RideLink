package com.ridelink.ride.validation;

import com.ridelink.ride.enums.RideStatus;
import com.ridelink.ride.exception.InvalidRideStatusException;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

@Component
public class RideStatusValidator {

    // Defines all valid transitions: from a given status, which target statuses are allowed.
    private static final Map<RideStatus, Set<RideStatus>> VALID_TRANSITIONS = Map.of(
            RideStatus.REQUESTED, Set.of(RideStatus.ASSIGNED, RideStatus.CANCELLED),
            RideStatus.ASSIGNED,  Set.of(RideStatus.ACCEPTED, RideStatus.CANCELLED),
            RideStatus.ACCEPTED,  Set.of(RideStatus.IN_PROGRESS, RideStatus.CANCELLED),
            RideStatus.IN_PROGRESS, Set.of(RideStatus.COMPLETED),
            RideStatus.COMPLETED, Set.of(),
            RideStatus.CANCELLED, Set.of()
    );

    public void validateTransition(RideStatus currentStatus, RideStatus targetStatus) {
        Set<RideStatus> allowed = VALID_TRANSITIONS.getOrDefault(currentStatus, Set.of());
        if (!allowed.contains(targetStatus)) {
            throw new InvalidRideStatusException(
                    "Invalid ride status transition from " + currentStatus + " to " + targetStatus);
        }
    }
}
