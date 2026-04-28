package com.cts.healthconnect.notification.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class NotificationDispatchResponseDto {

    private Long notificationId;
    private String status;      // DISPATCHED / FAILED
    private String channel;     // EMAIL / SMS / IN_APP
    private LocalDateTime timestamp;
}
