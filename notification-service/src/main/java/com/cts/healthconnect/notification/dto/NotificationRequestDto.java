package com.cts.healthconnect.notification.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class NotificationRequestDto {
    @NotNull(message = "Recipient ID is required")
    private Long recipientId;
    
    @NotBlank(message = "Recipient type (PATIENT/DOCTOR) is required")
    private String recipientType;

    // The message is now optional in the request because 
    // the service can generate it dynamically based on the type.
    private String message;

    @NotBlank(message = "Notification type is required")
    private String notificationType; // APPOINTMENT, INVOICE, BILLING, ADMISSION
    
    private String recipientEmail;
    private String recipientPhone;
}