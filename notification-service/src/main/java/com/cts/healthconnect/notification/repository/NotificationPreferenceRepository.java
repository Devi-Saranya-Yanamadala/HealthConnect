package com.cts.healthconnect.notification.repository;

import com.cts.healthconnect.notification.entity.NotificationPreference;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NotificationPreferenceRepository
        extends JpaRepository<NotificationPreference, Long> {

    Optional<NotificationPreference>
    findByRecipientIdAndRecipientTypeAndNotificationType(
            Long recipientId,
            String recipientType,
            String notificationType
    );
}