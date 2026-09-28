package com.ridesystem.payment.repository;

import com.ridesystem.payment.model.Payment;
import com.ridesystem.payment.model.PaymentStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data MongoDB repository for {@link Payment} documents.
 *
 * <p>Provides standard CRUD operations inherited from {@link MongoRepository},
 * plus custom query methods for the Fare Payment Service use cases.
 *
 * <p><strong>Database:</strong> {@code fare_payment_db} — dedicated, isolated.
 * This repository does NOT interact with any other microservice database.
 *
 * <p>IT3130 AD Group Assignment — RideLink System
 */
@Repository
public interface PaymentRepository extends MongoRepository<Payment, String> {

    /**
     * Find a payment by its associated ride ID.
     *
     * <p>Because {@code rideId} carries a unique index, at most one result
     * will ever be returned. This method is used by the service layer for
     * duplicate-payment detection and ride-based payment lookup.
     *
     * @param rideId the ride identifier to search for
     * @return an {@link Optional} containing the payment if found, or empty
     */
    Optional<Payment> findByRideId(String rideId);

    /**
     * Find all payments made by a specific passenger.
     *
     * @param passengerId the passenger identifier
     * @return list of payments for the given passenger (may be empty)
     */
    List<Payment> findByPassengerId(String passengerId);

    /**
     * Find all payments associated with a specific driver.
     *
     * @param driverId the driver identifier
     * @return list of payments for the given driver (may be empty)
     */
    List<Payment> findByDriverId(String driverId);

    /**
     * Find all payments matching a given status.
     *
     * @param paymentStatus the status to filter by
     * @return list of payments in the given status (may be empty)
     */
    List<Payment> findByPaymentStatus(PaymentStatus paymentStatus);

    /**
     * Find all payments for a passenger filtered by a specific status.
     *
     * @param passengerId   the passenger identifier
     * @param paymentStatus the status to filter by
     * @return list of matching payments (may be empty)
     */
    List<Payment> findByPassengerIdAndPaymentStatus(String passengerId, PaymentStatus paymentStatus);

    /**
     * Find all payments for a driver filtered by a specific status.
     *
     * @param driverId      the driver identifier
     * @param paymentStatus the status to filter by
     * @return list of matching payments (may be empty)
     */
    List<Payment> findByDriverIdAndPaymentStatus(String driverId, PaymentStatus paymentStatus);

    /**
     * Check whether a payment already exists for a given ride.
     *
     * <p>Used by the service layer as the first line of duplicate-payment
     * prevention before attempting to create a new {@link Payment} document.
     *
     * @param rideId the ride identifier to check
     * @return {@code true} if a payment for this ride already exists
     */
    boolean existsByRideId(String rideId);

    /**
     * Find a payment by its external transaction reference.
     *
     * <p>Useful for reconciliation with payment gateways (CARD / WALLET flows).
     *
     * @param transactionReference the external reference string
     * @return an {@link Optional} containing the payment if found, or empty
     */
    Optional<Payment> findByTransactionReference(String transactionReference);
}
