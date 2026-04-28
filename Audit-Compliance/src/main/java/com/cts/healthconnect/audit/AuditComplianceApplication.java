package com.cts.healthconnect.audit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Main Application class for the Audit & Compliance Service.
 * This service acts as a Eureka Client, registering itself with 
 * the Service Registry on port 8761.
 */
@SpringBootApplication
@EnableDiscoveryClient
public class AuditComplianceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuditComplianceApplication.class, args);
    }
}