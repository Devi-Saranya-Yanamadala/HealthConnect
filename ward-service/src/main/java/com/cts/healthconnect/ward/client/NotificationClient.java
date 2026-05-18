package com.cts.healthconnect.ward.client;

import com.cts.healthconnect.ward.dto.NotificationRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notification-service", url = "http://localhost:5004")
public interface NotificationClient {

    @PostMapping("/api/notifications")
    void sendNotification(@RequestBody NotificationRequestDto dto);
}