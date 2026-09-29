package com.ridesystem.payment;

import com.mongodb.client.MongoClient;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;

/**
 * Spring Boot context-load test for the Fare Payment Service.
 *
 * <p>Uses {@code @MockBean} to replace the real {@link MongoClient} with a Mockito mock,
 * so the full application context (including {@code @EnableMongoAuditing},
 * repository scanning, and Swagger config) can be verified without needing
 * a live MongoDB instance or any network download.
 *
 * <p>What this test verifies:
 * <ul>
 *     <li>Spring Boot application context loads without errors</li>
 *     <li>MongoDB Auditing ({@code @EnableMongoAuditing}) initialises correctly</li>
 *     <li>{@code PaymentRepository} interface is picked up by repository scanning</li>
 *     <li>{@code OpenApiConfig} Swagger bean is created successfully</li>
 *     <li>All package structures are valid and importable</li>
 * </ul>
 *
 */
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
class FarePaymentServiceApplicationTests {

    /**
     * Mock the MongoClient so Spring does not attempt a real connection
     * to MongoDB during the context-load test.
     */
    @MockBean
    private MongoClient mongoClient;

    @Test
    void contextLoads() {
        // Verifies that the Spring ApplicationContext loads without errors.
    }
}
