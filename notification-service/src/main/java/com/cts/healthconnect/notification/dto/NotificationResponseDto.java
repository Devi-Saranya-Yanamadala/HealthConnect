package com.cts.healthconnect.notification.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class NotificationResponseDto {
    private Long id;
    private String message;
    private String status;
    private LocalDateTime createdAt;
}