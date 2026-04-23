package com.cts.healthconnect.notification.client;

import com.cts.healthconnect.notification.dto.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

// Note: No 'url' attribute here. Feign uses 'name' to find the service in Eureka.
@FeignClient(name = "notification-service")
public interface NotificationFeignClient {

    @PostMapping("/api/notifications")
    NotificationResponseDto sendNotification(@RequestBody NotificationRequestDto dto);
}