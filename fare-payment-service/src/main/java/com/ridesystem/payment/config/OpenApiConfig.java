package com.ridesystem.payment.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * OpenAPI / Swagger UI Configuration for Fare Payment Service.
 *
 * <p>Swagger UI is accessible at: http://localhost:8083/swagger-ui/index.html
 * <p>OpenAPI JSON spec is accessible at: http://localhost:8083/v3/api-docs
 *
 * <p>IT3130 AD Group Assignment — RideLink System
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI farePaymentServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Fare Payment Service API")
                        .description(
                                "REST API for the Fare Payment Service of the RideLink System.\n\n" +
                                "**Responsibilities:**\n" +
                                "- Fare calculation\n" +
                                "- Create and process payments\n" +
                                "- Track payment status (PENDING, COMPLETED, FAILED, REFUNDED)\n" +
                                "- Payment history retrieval\n" +
                                "- Search by Payment ID, Ride ID, or Passenger ID\n" +
                                "- Refund processing\n" +
                                "- Duplicate payment prevention\n\n" +
                                "**Supported Payment Methods:** CASH, CARD, WALLET\n\n" +
                                "**Database:** fare_payment_db (MongoDB) — dedicated isolated database\n\n" +
                                "_IT3130 AD Group Assignment_"
                        )
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("RideLink Development Team")
                                .email("support@ridesystem.com"))
                        .license(new License()
                                .name("IT3130 Academic Use")
                                .url("https://github.com/uvindushehan/RideLink")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8083")
                                .description("Local Development Server")
                ));
    }
}
