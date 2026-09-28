package com.ridesystem.payment;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

/**
 * Integration test to verify that the Spring application context loads
 * successfully without errors.
 *
 * <p>Uses a test property source to skip actual MongoDB connection during tests.
 *
 * <p>IT3130 AD Group Assignment — RideLink System
 */
@SpringBootTest
@TestPropertySource(properties = {
        "spring.data.mongodb.uri=mongodb://localhost:27017/fare_payment_test_db",
        "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration," +
                "org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration"
})
class FarePaymentServiceApplicationTests {

    @Test
    void contextLoads() {
        // Verifies that the Spring ApplicationContext loads successfully.
    }
}
