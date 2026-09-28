package com.ridemanagement.accountservice.repository;

import com.ridemanagement.accountservice.model.Account;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for managing persistence and queries on Account entities in MongoDB.
 */
@Repository
public interface AccountRepository extends MongoRepository<Account, String> {

    /**
     * Find an account by email address.
     *
     * @param email user's email
     * @return Optional containing the account if found, or empty
     */
    Optional<Account> findByEmail(String email);

    /**
     * Find an account by phone number.
     *
     * @param phoneNumber user's phone number
     * @return Optional containing the account if found, or empty
     */
    Optional<Account> findByPhoneNumber(String phoneNumber);

    /**
     * Check if an account exists with the given email address.
     *
     * @param email user's email
     * @return true if an account exists with this email, false otherwise
     */
    boolean existsByEmail(String email);

    /**
     * Check if an account exists with the given phone number.
     *
     * @param phoneNumber user's phone number
     * @return true if an account exists with this phone number, false otherwise
     */
    boolean existsByPhoneNumber(String phoneNumber);
}
