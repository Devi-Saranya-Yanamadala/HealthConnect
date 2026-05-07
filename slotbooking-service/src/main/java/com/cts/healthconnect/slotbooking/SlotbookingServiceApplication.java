package com.cts.healthconnect.slotbooking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.cts.healthconnect.slotbooking.client") // ✅ ADDED
public class SlotbookingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SlotbookingServiceApplication.class, args);
    }
}