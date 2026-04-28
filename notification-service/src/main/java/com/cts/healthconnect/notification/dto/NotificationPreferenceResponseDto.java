
package com.cts.healthconnect.notification.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotificationPreferenceResponseDto {

    private Long id;
    private Long recipientId;
    private String recipientType;
    private String notificationType;

    private boolean emailEnabled;
    private boolean smsEnabled;
    private boolean inAppEnabled;
}