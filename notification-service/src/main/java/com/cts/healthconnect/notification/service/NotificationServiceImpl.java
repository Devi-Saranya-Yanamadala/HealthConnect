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

        // ✅ EMAIL ONLY — no SMS service
        if (emailEnabled && dto.getRecipientEmail() != null
                && !dto.getRecipientEmail().isBlank()) {

            String subject = buildEmailSubject(type);
            log.info("➡️ Dispatching EMAIL for type {} to {}", type, dto.getRecipientEmail());

            emailService.sendEmail(
                    dto.getRecipientEmail(),
                    subject,
                    message
            );
        } else {
            log.info("ℹ️ Email skipped — no email address provided or disabled for type {}", type);
        }
    }

    private String buildEmailSubject(String type) {
        return switch (type) {
            case "APPOINTMENT"  -> "HealthConnect — Appointment Confirmation";
            case "ADMISSION"    -> "HealthConnect — Hospital Admission Notice";
            case "BILLING"      -> "HealthConnect — Invoice Generated";
            case "INVOICE"      -> "HealthConnect — Payment Invoice";
            case "PRESCRIPTION" -> "HealthConnect — Prescription Issued";
            default             -> "HealthConnect — Notification";
        };
    }

    private String generateDynamicMessage(NotificationRequestDto dto) {
        String recipientType = dto.getRecipientType() != null
                ? dto.getRecipientType() : "User";

        return switch (dto.getNotificationType().toUpperCase()) {

            case "APPOINTMENT" -> """
                Dear %s,

                Your appointment has been successfully scheduled at HealthConnect Hospital.

                Please arrive 15 minutes before your scheduled time and carry a valid ID and any previous medical records.

                If you need to reschedule or cancel, please contact us at least 24 hours in advance.

                Thank you for choosing HealthConnect Hospital.

                Warm regards,
                HealthConnect Medical Team
                """.formatted(recipientType);

            case "ADMISSION" -> """
                Dear %s,

                You have been successfully admitted to HealthConnect Hospital.

                Our medical team will attend to you shortly. Please ensure your emergency contact details are updated with the front desk.

                For any immediate assistance, please contact the nurse station.

                Wishing you a speedy recovery.

                Warm regards,
                HealthConnect Medical Team
                """.formatted(recipientType);

            case "BILLING" -> """
                Dear %s,

                Your invoice has been generated at HealthConnect Hospital.

                Please review your billing details and complete the payment at the billing counter or via our online portal.

                For any billing queries, please contact our billing department.

                Thank you for choosing HealthConnect Hospital.

                Warm regards,
                HealthConnect Billing Department
                """.formatted(recipientType);

            case "INVOICE" -> """
                Dear %s,

                Your payment invoice from HealthConnect Hospital is ready.

                Kindly make the payment within the stipulated time to avoid any inconvenience. You may pay at the billing counter or through our authorized payment channels.

                Please retain this notification for your records.

                Warm regards,
                HealthConnect Billing Department
                """.formatted(recipientType);

            case "PRESCRIPTION" -> """
                Dear %s,

                Your prescription has been issued by your doctor at HealthConnect Hospital.

                Please collect your prescription from the pharmacy counter. Ensure you follow the dosage instructions as prescribed.

                For any queries regarding your prescription, please consult your doctor.

                Take care and get well soon.

                Warm regards,
                HealthConnect Medical Team
                """.formatted(recipientType);

            default -> """
                Dear %s,

                You have a new notification from HealthConnect Hospital.

                Please log in to the HealthConnect portal for more details.

                Warm regards,
                HealthConnect Team
                """.formatted(recipientType);
        };
    }

    private NotificationResponseDto mapToResponse(Notification n) {
        return NotificationResponseDto.builder()
                .id(n.getId())
                .message(n.getMessage())
                .notificationType(n.getNotificationType())
                .recipientType(n.getRecipientType())         
                .read(n.getStatus() == NotificationStatus.READ)
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
        repository.save(n);
    }
}