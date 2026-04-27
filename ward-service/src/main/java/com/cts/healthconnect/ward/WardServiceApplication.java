package com.cts.healthconnect.ward;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class WardServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(WardServiceApplication.class, args);
	}

}
