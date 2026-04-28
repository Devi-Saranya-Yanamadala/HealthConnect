package com.cts.healthconnect.notification.service;

import com.cts.healthconnect.notification.dto.*;
import com.cts.healthconnect.notification.entity.*;
import com.cts.healthconnect.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class NotificationDispatchServiceImpl
        implements NotificationDispatchService {

    private final NotificationRepository notificationRepository;
    private final NotificationService notificationService;

    @Override
    public NotificationDispatchResponseDto dispatch(
            NotificationDispatchRequestDto dto) {

        // Reuse existing notification logic
        NotificationResponseDto response =
                notificationService.sendNotification(
                        mapToNotificationRequest(dto)
                );

        return NotificationDispatchResponseDto.builder()
                .notificationId(response.getId())
                .status("DISPATCHED")
                .channel(determineChannel(dto))
                .timestamp(LocalDateTime.now())
                .build();
    }

    private NotificationRequestDto mapToNotificationRequest(
            NotificationDispatchRequestDto dto) {

        NotificationRequestDto request = new NotificationRequestDto();
        request.setRecipientId(dto.getRecipientId());
        request.setRecipientType(dto.getRecipientType());
        request.setNotificationType(dto.getNotificationType());
        request.setMessage(dto.getMessage());
        request.setRecipientEmail(dto.getRecipientEmail());
        request.setRecipientPhone(dto.getRecipientPhone());
        return request;
    }

    private String determineChannel(NotificationDispatchRequestDto dto) {
        if (dto.getRecipientEmail() != null) return "EMAIL";
        if (dto.getRecipientPhone() != null) return "SMS";
        return "IN_APP";
    }
}
