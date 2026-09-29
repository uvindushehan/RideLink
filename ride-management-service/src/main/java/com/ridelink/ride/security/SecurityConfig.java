package com.ridelink.ride.security;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .exceptionHandling(exceptions -> exceptions
                .authenticationEntryPoint((request, response, authException) -> 
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized"))
                .accessDeniedHandler((request, response, accessDeniedException) -> 
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden"))
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authz -> authz
                // Swagger UI
                .requestMatchers("/v3/api-docs", "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html", "/swagger-resources", "/swagger-resources/**", "/configuration/ui", "/configuration/security", "/webjars/**", "/swagger-ui/index.html").permitAll()
                // Ride Management Endpoints
                .requestMatchers(HttpMethod.POST, "/api/rides").hasAnyRole("PASSENGER", "ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/rides/{id}").authenticated()
                .requestMatchers(HttpMethod.PATCH, "/api/rides/{id}/assign-driver").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/rides/available-drivers").authenticated()
                .requestMatchers(HttpMethod.PATCH, "/api/rides/{id}/auto-assign-driver").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PATCH, "/api/rides/{id}/accept").hasAnyRole("DRIVER", "ADMIN")
                .requestMatchers(HttpMethod.PATCH, "/api/rides/{id}/start").hasAnyRole("DRIVER", "ADMIN")
                .requestMatchers(HttpMethod.PATCH, "/api/rides/{id}/complete").hasAnyRole("DRIVER", "ADMIN")
                .requestMatchers(HttpMethod.PATCH, "/api/rides/{id}/cancel").hasAnyRole("PASSENGER", "ADMIN")
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
