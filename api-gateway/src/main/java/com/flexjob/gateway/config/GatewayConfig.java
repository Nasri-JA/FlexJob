package com.flexjob.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // User Service Routes
                .route("user-service", r -> r
                        .path("/api/auth/**", "/api/users/**")
                        .uri("lb://user-service"))

                // Job Service Routes
                .route("job-service", r -> r
                        .path("/api/jobs/**", "/api/categories/**")
                        .uri("lb://job-service"))

                // Engagement Service Routes (applications, bookings, reviews)
                .route("engagement-service", r -> r
                        .path("/api/applications/**", "/api/bookings/**", "/api/reviews/**")
                        .uri("lb://engagement-service"))

                // Payment Service Routes
                .route("payment-service", r -> r
                        .path("/api/payments/**", "/api/invoices/**")
                        .uri("lb://payment-service"))

                // Notification Service Routes
                .route("notification-service", r -> r
                        .path("/api/notifications/**")
                        .uri("lb://notification-service"))

                .build();
    }
}
