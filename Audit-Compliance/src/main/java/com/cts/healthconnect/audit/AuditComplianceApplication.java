package com.cts.healthconnect.audit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer 
public class AuditComplianceApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuditComplianceApplication.class, args);
    }
}