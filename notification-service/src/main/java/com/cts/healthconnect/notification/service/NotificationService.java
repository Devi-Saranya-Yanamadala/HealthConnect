package com.cts.healthconnect.notification.service;



import com.cts.healthconnect.notification.dto.*;

import java.util.List;

public interface NotificationService {

    NotificationResponseDto sendNotification(NotificationRequestDto dto);

    List<NotificationResponseDto> getNotifications(Long recipientId, String recipientType);

    void markAsRead(Long notificationId);
}