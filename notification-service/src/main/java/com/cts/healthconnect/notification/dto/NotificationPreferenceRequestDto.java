
package com.cts.healthconnect.notification.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class NotificationPreferenceRequestDto {

    @NotNull
    private Long recipientId;

    @NotBlank
    private String recipientType;

    @NotBlank
    private String notificationType;

    private boolean emailEnabled;
    private boolean smsEnabled;
    private boolean inAppEnabled;
}
