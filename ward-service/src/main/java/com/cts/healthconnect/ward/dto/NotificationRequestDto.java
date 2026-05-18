package com.cts.healthconnect.ward.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotificationRequestDto {
    private Long   recipientId;
    private String recipientType;
    private String notificationType;
    private String message;
    private String recipientEmail;
    private String recipientPhone;
}