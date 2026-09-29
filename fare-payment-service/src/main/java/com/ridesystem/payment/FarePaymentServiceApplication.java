package com.ridesystem.payment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Fare Payment Service - Main Application Entry Point
 *
 * <p>This microservice is responsible for:
 * <ul>
 *     <li>Fare calculation</li>
 *     <li>Creating and processing payments</li>
 *     <li>Tracking payment status</li>
 *     <li>Payment history management</li>
 *     <li>Refund processing</li>
 *     <li>Duplicate payment prevention</li>
 * </ul>
 *
 * <p><strong>Database:</strong> fare_payment_db (MongoDB) — dedicated, isolated database.
 * <p><strong>Port:</strong> 8083
 * <p><strong>Swagger UI:</strong> http://localhost:8083/swagger-ui/index.html
 *
 * <p>{@code @EnableMongoAuditing} activates Spring Data MongoDB auditing so that
 * {@code @CreatedDate} and {@code @LastModifiedDate} on {@link com.ridesystem.payment.model.Payment}
 * are automatically populated on save/update operations.
 *
 * <p>IT3130 AD Group Assignment — RideLink System
 */
@SpringBootApplication
public class FarePaymentServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FarePaymentServiceApplication.class, args);
    }
}
