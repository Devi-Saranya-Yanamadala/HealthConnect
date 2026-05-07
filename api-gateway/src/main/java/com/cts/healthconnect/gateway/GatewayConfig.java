package com.cts.healthconnect.gateway;

import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRoutes(RouteLocatorBuilder builder) {
        return builder.routes()

            .route("auth", r -> r.path("/api/auth/**")
                .uri("http://localhost:5006"))

            .route("doctor", r -> r.path("/api/doctors/**")
                .uri("http://localhost:5000"))

            .route("slot", r -> r.path("/api/slots/**")
                .uri("http://localhost:5001"))

            .route("appointment", r -> r.path("/api/appointments/**")
                .uri("http://localhost:5002"))

            .route("patient", r -> r.path("/api/patients/**")
                .uri("http://localhost:5003"))

            .route("notification", r -> r.path("/api/notifications/**")
                .uri("http://localhost:5004"))

            .route("invoice", r -> r.path("/api/invoices/**")
                .uri("http://localhost:5005"))

            .route("claim", r -> r.path("/api/claims/**")
                .uri("http://localhost:5005"))

            .route("ward", r -> r.path("/api/wards/**")
                .uri("http://localhost:5007"))

            .route("analytics", r -> r.path("/api/analytics/**")
                .uri("http://localhost:5008"))

            // ✅ FIXED: was named "analytics" (duplicate) with wrong path
            .route("audit", r -> r.path("/api/audit/**")
                .uri("http://localhost:5010"))

            // ✅ ADDED: was completely missing
            .route("compliance", r -> r.path("/api/compliance/**")
                .uri("http://localhost:5010"))

            .build();
    }
}