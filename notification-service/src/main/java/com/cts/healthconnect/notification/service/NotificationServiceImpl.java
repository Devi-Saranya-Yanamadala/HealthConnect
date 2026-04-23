package com.cts.healthconnect.notification.service;

import com.cts.healthconnect.notification.dto.*;
import com.cts.healthconnect.notification.entity.*;
import com.cts.healthconnect.notification.exception.NotificationNotFoundException;
import com.cts.healthconnect.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository repository;

    @Override
    public NotificationResponseDto sendNotification(NotificationRequestDto dto) {
        Notification notification = Notification.builder()
                .recipientId(dto.getRecipientId())
                .recipientType(dto.getRecipientType())
                .notificationType(dto.getNotificationType())
                .message(dto.getMessage())
                .status(NotificationStatus.SENT)
                .build();
        repository.save(notification);
        return map(notification);
    }

    @Override
    public List<NotificationResponseDto> getNotifications(Long recipientId, String recipientType) {
        return repository.findByRecipientIdAndRecipientType(recipientId, recipientType)
                .stream().map(this::map).toList();
    }

    @Override
    public void markAsRead(Long notificationId) {
        Notification notification = repository.findById(notificationId)
                .orElseThrow(() -> new NotificationNotFoundException(notificationId));
        notification.setStatus(NotificationStatus.READ);
    }

    private NotificationResponseDto map(Notification n) {
        return NotificationResponseDto.builder()
                .id(n.getId())
                .message(n.getMessage())
                .status(n.getStatus().name())
                .createdAt(n.getCreatedAt())
                .build();
    }
}