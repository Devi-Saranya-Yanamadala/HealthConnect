package com.cts.healthconnect.ward;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.cts.healthconnect.ward.client")
public class WardServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(WardServiceApplication.class, args);
    }
}