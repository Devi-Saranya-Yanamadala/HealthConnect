package com.cts.healthconnect.notification.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class NotificationDispatchRequestDto {

    @NotNull
    private Long recipientId;

    @NotBlank
    private String recipientType;  // PATIENT / DOCTOR / ADMIN

    @NotBlank
    private String notificationType; // APPOINTMENT, BILLING, ADMISSION

    // optional – auto-generated if not sent
    private String message;

    private String recipientEmail;
    private String recipientPhone;

    // internal metadata
    private String triggeredBy; // APPOINTMENT_SERVICE, SYSTEM, ADMIN
}
