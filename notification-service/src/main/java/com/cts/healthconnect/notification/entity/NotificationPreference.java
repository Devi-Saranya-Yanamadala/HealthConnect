
package com.cts.healthconnect.notification.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
    name = "notification_preferences",
    uniqueConstraints = @UniqueConstraint(
        columnNames = {"recipientId", "recipientType", "notificationType"}
    )
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationPreference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long recipientId;
    private String recipientType;
    private String notificationType;

    private boolean emailEnabled;
    private boolean smsEnabled;
    private boolean inAppEnabled;
}