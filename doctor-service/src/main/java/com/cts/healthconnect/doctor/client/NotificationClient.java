package com.cts.healthconnect.doctor.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notification-service")
public interface NotificationClient {

    @PostMapping("/api/notifications")
    void send(@RequestBody NotificationRequest request);

    record NotificationRequest(
        Long   recipientId,
        String recipientType,
        String notificationType,
        String message,
        String recipientEmail,
        String recipientPhone
    ) {}
}