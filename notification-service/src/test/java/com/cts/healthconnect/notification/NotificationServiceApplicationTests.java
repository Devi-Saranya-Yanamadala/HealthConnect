package com.cts.healthconnect.notification;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import com.cts.healthconnect.notification.controller.NotificationController;
import com.cts.healthconnect.notification.service.NotificationService;

@SpringBootTest
class NotificationServiceApplicationTests {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private NotificationController notificationController;

    @Autowired
    private NotificationService notificationService;

    @Test
    @DisplayName("Check if Spring Application Context loads successfully")
    void contextLoads() {
        // This test will fail if the database connection (MySQL) 
        // or properties file is incorrectly configured.
        assertThat(applicationContext).isNotNull();
    }

    @Test
    @DisplayName("Verify that Notification Controller is initialized")
    void controllerIsNotNull() {
        // Ensures the REST layer is ready to receive requests from Appointment Service
        assertThat(notificationController).isNotNull();
    }

    @Test
    @DisplayName("Verify that Notification Service is initialized")
    void serviceIsNotNull() {
        // Ensures the business logic layer is correctly injected
        assertThat(notificationService).isNotNull();
    }
}