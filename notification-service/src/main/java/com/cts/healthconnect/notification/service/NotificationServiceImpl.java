package com.cts.healthconnect.notification.service;

import com.cts.healthconnect.notification.dto.*;
import com.cts.healthconnect.notification.entity.*;
import com.cts.healthconnect.notification.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository repository;
    private final NotificationPreferenceRepository preferenceRepository;
    private final EmailService emailService;   // ✅ NEW

    @Override
    public NotificationResponseDto sendNotification(NotificationRequestDto dto) {

        String message =
                (dto.getMessage() != null && !dto.getMessage().isBlank())
                        ? dto.getMessage()                     // ✅ USER MESSAGE
                        : generateDynamicMessage(dto);         // ✅ FALLBACK

        Notification notification = Notification.builder()
                .recipientId(dto.getRecipientId())
                .recipientType(dto.getRecipientType())
                .notificationType(dto.getNotificationType().toUpperCase())
                .message(message)
                .status(NotificationStatus.SENT)
                .build();

        Notification saved = repository.save(notification);

        dispatchToMediums(dto, message);

        return mapToResponse(saved);
    }
    private void dispatchToMediums(NotificationRequestDto dto, String message) {

        String type = dto.getNotificationType().toUpperCase();

        NotificationPreference preference =
                preferenceRepository
                        .findByRecipientIdAndRecipientTypeAndNotificationType(
                                dto.getRecipientId(),
                                dto.getRecipientType(),
                                type
                        )
                        .orElse(null);

        boolean emailEnabled = preference == null || preference.isEmailEnabled();
        boolean smsEnabled   = preference == null || preference.isSmsEnabled();

        // ✅ SMS notifications
        if (smsEnabled && (type.equals("APPOINTMENT") || type.equals("ADMISSION"))) {
            sendSmsAlert(dto.getRecipientPhone(), message);
        }

        // ✅ EMAIL notifications (ASYNC via EmailService)
        if (emailEnabled && (type.equals("BILLING")
                || type.equals("INVOICE")
                || type.equals("PRESCRIPTION"))) {
        	log.info("➡️ About to send email for type {}", type);

            emailService.sendEmail(
                    dto.getRecipientEmail(),
                    "HealthConnect Notification: " + type,
                    message
            );
        }
    }

    private void sendSmsAlert(String phone, String message) {
        if (phone == null || phone.isBlank()) return;
        log.info("📩 SMS SENT → {} : {}", phone, message);
    }

    private String generateDynamicMessage(NotificationRequestDto dto) {
        return switch (dto.getNotificationType().toUpperCase()) {
            case "APPOINTMENT" ->
                    "HealthConnect: Appointment confirmed.";
            case "ADMISSION" ->
                    "HealthConnect: Admission confirmed.";
            case "BILLING", "INVOICE" ->
                    "HealthConnect: Invoice generated.";
            default ->
                    "HealthConnect: New notification.";
        };
    }

    private NotificationResponseDto mapToResponse(Notification n) {
        return NotificationResponseDto.builder()
                .id(n.getId())
                .message(n.getMessage())
                .status(n.getStatus().name())
                .createdAt(n.getCreatedAt())
                .build();
    }

    @Override
    public List<NotificationResponseDto> getNotifications(Long recipientId, String recipientType) {
        return repository.findByRecipientIdAndRecipientType(recipientId, recipientType)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void markAsRead(Long id) {
        Notification n = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        n.setStatus(NotificationStatus.READ);
    }
}