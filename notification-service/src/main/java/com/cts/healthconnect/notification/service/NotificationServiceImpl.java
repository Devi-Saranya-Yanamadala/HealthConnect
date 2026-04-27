package com.cts.healthconnect.notification.service;

import com.cts.healthconnect.notification.dto.NotificationRequestDto;
import com.cts.healthconnect.notification.dto.NotificationResponseDto;
import com.cts.healthconnect.notification.entity.Notification;
import com.cts.healthconnect.notification.entity.NotificationStatus;
import com.cts.healthconnect.notification.exception.NotificationNotFoundException;
import com.cts.healthconnect.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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
    private final JavaMailSender mailSender;

    @Override
    public NotificationResponseDto sendNotification(NotificationRequestDto dto) {
        log.info("Processing notification request for type: {}", dto.getNotificationType());

        // 1. Dynamic Message Generation & Validation
        String finalMessage = generateDynamicMessage(dto);

        // 2. Persist to Database
        Notification notification = Notification.builder()
                .recipientId(dto.getRecipientId())
                .recipientType(dto.getRecipientType())
                .notificationType(dto.getNotificationType().toUpperCase())
                .message(finalMessage)
                .status(NotificationStatus.SENT)
                .build();
        
        Notification savedNotification = repository.save(notification);

        // 3. Dispatch Alerts Based on Business Rules
        dispatchToMediums(dto, finalMessage);

        return mapToResponse(savedNotification);
    }

    private String generateDynamicMessage(NotificationRequestDto dto) {
        // If the calling service (Appointment/Billing) provided a specific message, use it.
        if (dto.getMessage() != null && !dto.getMessage().trim().isEmpty()) {
            return dto.getMessage();
        }

        // Real-world logic: Apply templates for each microservice trigger
        return switch (dto.getNotificationType().toUpperCase()) {
            case "APPOINTMENT" -> 
                "HealthConnect: Your appointment is confirmed. OP Number: " + generateOpNumber();
            
            case "ADMISSION" -> 
                "HealthConnect: Patient Admission confirmed. Please report to the Front Desk for Ward details.";
            
            case "BILLING", "INVOICE" -> 
                "HealthConnect: A new invoice has been generated for your recent visit. Please check your registered email for payment details.";
            
            case "PRESCRIPTION" -> 
                "HealthConnect: Your digital prescription is ready. View it in the Patient Portal.";
            
            default -> "HealthConnect: You have a new update regarding your healthcare services.";
        };
    }

    private void dispatchToMediums(NotificationRequestDto dto, String message) {
        String type = dto.getNotificationType().toUpperCase();

        // Rule: Urgent alerts go to Mobile (SMS)
        if (type.equals("APPOINTMENT") || type.equals("ADMISSION")) {
            sendSmsAlert(dto.getRecipientPhone(), message);
        }

        // Rule: Document-heavy alerts go to Email
        if (type.equals("BILLING") || type.equals("INVOICE") || type.equals("PRESCRIPTION")) {
            sendEmailAlert(dto.getRecipientEmail(), "HealthConnect Update: " + type, message);
        }
    }

    private void sendEmailAlert(String to, String subject, String body) {
        if (to == null || to.isEmpty()) {
            log.warn("Skipping Email dispatch: No recipient email provided.");
            return;
        }
        try {
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setTo(to);
            mail.setSubject(subject);
            mail.setText(body);
            mailSender.send(mail);
            log.info("Email successfully dispatched to {}", to);
        } catch (Exception e) {
            log.error("Failed to send email to {}: {}", to, e.getMessage());
        }
    }

    private void sendSmsAlert(String phone, String message) {
        if (phone == null || phone.isEmpty()) {
            log.warn("Skipping SMS dispatch: No phone number provided.");
            return;
        }
        // Integration point for Twilio or Vonage
        log.info("SMS DISPATCHED to {}: {}", phone, message);
    }

    private String generateOpNumber() {
        return "HC-OP-" + (System.currentTimeMillis() % 10000);
    }

    @Override
    public List<NotificationResponseDto> getNotifications(Long recipientId, String recipientType) {
        return repository.findByRecipientIdAndRecipientType(recipientId, recipientType)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void markAsRead(Long notificationId) {
        Notification notification = repository.findById(notificationId)
                .orElseThrow(() -> new NotificationNotFoundException(notificationId));
        notification.setStatus(NotificationStatus.READ);
        repository.save(notification);
    }

    private NotificationResponseDto mapToResponse(Notification n) {
        return NotificationResponseDto.builder()
                .id(n.getId())
                .message(n.getMessage())
                .status(n.getStatus().name())
                .createdAt(n.getCreatedAt())
                .build();
    }
}