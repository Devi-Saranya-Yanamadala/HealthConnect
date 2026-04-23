package com.cts.healthconnect.notification.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequestDto {
    private Long recipientId;
    private String recipientType; // DOCTOR or PATIENT
    private String message;
    private String notificationType; // APPOINTMENT, BILLING, etc.
}