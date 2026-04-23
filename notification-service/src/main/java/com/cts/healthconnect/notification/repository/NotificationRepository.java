package com.cts.healthconnect.notification.repository;

import com.cts.healthconnect.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByRecipientIdAndRecipientType(Long recipientId, String recipientType);
}