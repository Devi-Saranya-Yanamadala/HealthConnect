package com.cts.healthconnect.notification.service;

import com.cts.healthconnect.notification.dto.*;
import com.cts.healthconnect.notification.entity.NotificationPreference;
import com.cts.healthconnect.notification.repository.NotificationPreferenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationPreferenceServiceImpl
        implements NotificationPreferenceService {

    private final NotificationPreferenceRepository repository;

    @Override
    public NotificationPreferenceResponseDto savePreference(
            NotificationPreferenceRequestDto dto) {

        NotificationPreference preference =
                repository.findByRecipientIdAndRecipientTypeAndNotificationType(
                        dto.getRecipientId(),
                        dto.getRecipientType(),
                        dto.getNotificationType().toUpperCase()
                ).orElse(NotificationPreference.builder()
                        .recipientId(dto.getRecipientId())
                        .recipientType(dto.getRecipientType())
                        .notificationType(dto.getNotificationType().toUpperCase())
                        .build());

        preference.setEmailEnabled(dto.isEmailEnabled());
        preference.setSmsEnabled(dto.isSmsEnabled());
        preference.setInAppEnabled(dto.isInAppEnabled());

        NotificationPreference saved = repository.save(preference);

        return NotificationPreferenceResponseDto.builder()
                .id(saved.getId())
                .recipientId(saved.getRecipientId())
                .recipientType(saved.getRecipientType())
                .notificationType(saved.getNotificationType())
                .emailEnabled(saved.isEmailEnabled())
                .smsEnabled(saved.isSmsEnabled())
                .inAppEnabled(saved.isInAppEnabled())
                .build();
    }
}