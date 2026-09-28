package com.ridelink.ride.client;

import com.ridelink.ride.dto.external.AvailableDriverResponse;
import com.ridelink.ride.exception.DriverServiceUnavailableException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class DriverServiceClient {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public DriverServiceClient(@Value("${driver-service.base-url}") String baseUrl) {
        this.restTemplate = new RestTemplate();
        this.baseUrl = baseUrl;
    }

    public List<AvailableDriverResponse> getAvailableDrivers() {
        try {
            ResponseEntity<List<AvailableDriverResponse>> response = restTemplate.exchange(
                    baseUrl + "/api/drivers/available",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<AvailableDriverResponse>>() {}
            );
            return response.getBody();
        } catch (Exception e) {
            throw new DriverServiceUnavailableException("Driver & Vehicle Service is currently unavailable");
        }
    }
}
