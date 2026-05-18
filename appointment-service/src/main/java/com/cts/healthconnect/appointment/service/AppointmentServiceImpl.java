package com.cts.healthconnect.appointment.service;

import com.cts.healthconnect.appointment.client.AuditClient;
import com.cts.healthconnect.appointment.client.NotificationClient;
import com.cts.healthconnect.appointment.client.PatientClient;
import com.cts.healthconnect.appointment.client.SlotBookingClient;
import com.cts.healthconnect.appointment.dto.*;
import com.cts.healthconnect.appointment.entity.*;
import com.cts.healthconnect.appointment.exception.AppointmentNotFoundException;
import com.cts.healthconnect.appointment.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository repository;
    private final SlotBookingClient     slotClient;
    private final AuditClient           auditClient;
    private final NotificationClient notificationClient;
    private final PatientClient patientClient;
    

    @Override
    public AppointmentResponseDto bookAppointment(AppointmentRequestDto dto) {
    	
    	// Fetch patient ID
    	Long patientId = null;
        try {
            patientId = patientClient.getPatient(dto.getPatientCode()).getPatient_id();
        } catch (Exception e) {
            throw new RuntimeException("Patient not found with code: " + dto.getPatientCode());
        }
    	
        boolean alreadyBooked =
                repository.existsByPatientIdAndDoctorCodeAndAppointmentDateAndStatus(
                        patientId,
                        dto.getDoctorCode(),
                        dto.getAppointmentDate(),
                        AppointmentStatus.BOOKED
                );

        if (alreadyBooked) {
            throw new IllegalStateException(
                    "Patient already has an appointment with this doctor for the selected date"
            );
        }

        
        // ← fetch email from patient-service
        String patientEmail = null;
        try {
            PatientResponseDto patient = patientClient.getPatientById(patientId);
            patientEmail = patient.getEmail();
        } catch (Exception e) {
            System.err.println(">>> Could not fetch patient email: " + e.getMessage());
        }

        slotClient.bookSlot(dto.getSlotId());

        try {
            Appointment appointment = Appointment.builder()
                    .appointmentCode(generateAppointmentCode())
                    .patientId(patientId)
                    .patientCode(dto.getPatientCode())
                    .doctorCode(dto.getDoctorCode())
                    .slotId(dto.getSlotId())
                    .appointmentDate(dto.getAppointmentDate())
                    .status(AppointmentStatus.BOOKED)
                    .build();

            repository.save(appointment);

            audit("BOOK_APPOINTMENT", appointment.getAppointmentCode(),
                  "Patient: " + dto.getPatientCode()
                  + " | Doctor: " + dto.getDoctorCode()
                  + " | Date: " + dto.getAppointmentDate());

            notify("APPOINTMENT", patientId, patientEmail,
                   "Your appointment with Dr. " + dto.getDoctorCode()
                   + " is confirmed for " + dto.getAppointmentDate() + ".");

            return mapToResponse(appointment);

        } catch (Exception e) {
            // ← release the slot if anything goes wrong
            try {
                slotClient.releaseSlot(dto.getSlotId());
                System.err.println(">>> Slot released due to booking failure: " + e.getMessage());
            } catch (Exception ex) {
                System.err.println(">>> Failed to release slot: " + ex.getMessage());
            }
            throw e;   // ← rethrow so the error still propagates to frontend
        }
    }
    
    

    @Override
    public AppointmentResponseDto cancelAppointmentById(Long appointmentId) {
        Appointment appointment = repository.findById(appointmentId)
                .orElseThrow(() ->
                        new AppointmentNotFoundException("Appointment not found"));
        return cancelInternal(appointment);
    }
    /*@Override
    public AppointmentResponseDto cancelAppointmentByCode(String appointmentCode) {
    	
        Appointment appointment = repository.findByAppointmentCode(appointmentCode)
                .orElseThrow(() -> new AppointmentNotFoundException(appointmentCode));
        return cancelInternal(appointment);
    }*/
    
    @Override
    public List<AppointmentResponseDto> getAppointmentsByPatientCode(String patientCode) {
        // fetch patientId from patient-service using patientCode
        Long patientId = null;
        try {
            patientId = patientClient.getPatient(patientCode).getPatient_id();
        } catch (Exception e) {
            throw new RuntimeException("Patient not found with code: " + patientCode);
        }

        return repository.findByPatientId(patientId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }
    

    private AppointmentResponseDto cancelInternal(Appointment appointment) {
        if (appointment.getStatus() != AppointmentStatus.BOOKED) {
            throw new IllegalStateException(
                    "Only BOOKED appointments can be cancelled");
        }

        slotClient.releaseSlot(appointment.getSlotId());
        appointment.setStatus(AppointmentStatus.CANCELLED);
        repository.save(appointment);
        
        // ← fetch email
        String patientEmail = null;
        try {
            patientEmail = patientClient.getPatientById(appointment.getPatientId()).getEmail();
        } catch (Exception e) {
            System.err.println(">>> Could not fetch patient email: " + e.getMessage());
        }

        // trigger notification
        notify("APPOINTMENT", appointment.getPatientId(), patientEmail,
                "Your appointment (code: " + appointment.getAppointmentCode()
                + ") has been cancelled.");
        

        // AUDIT LOG
        audit("CANCEL_APPOINTMENT", appointment.getAppointmentCode(),
              "Appointment cancelled");

        return mapToResponse(appointment);
    }

    @Override
    public AppointmentResponseDto rescheduleAppointment(
            AppointmentRescheduleRequestDto dto) {

        Appointment appointment = repository.findByAppointmentCode(dto.getAppointmentCode())
                .orElseThrow(() ->
                        new AppointmentNotFoundException(dto.getAppointmentCode()));

        if (appointment.getStatus() != AppointmentStatus.BOOKED) {
            throw new IllegalStateException(
                    "Only BOOKED appointments can be rescheduled");
        }

        Long oldSlotId = appointment.getSlotId();
        slotClient.releaseSlot(oldSlotId);

        try {
            slotClient.bookSlot(dto.getNewSlotId());
        } catch (Exception e) {
            slotClient.bookSlot(oldSlotId);
            throw e;
        }

        appointment.setSlotId(dto.getNewSlotId());
        appointment.setAppointmentDate(dto.getNewAppointmentDate());
        repository.save(appointment);
        
        // ← fetch email
        String patientEmail = null;
        try {
            patientEmail = patientClient.getPatientById(appointment.getPatientId()).getEmail();
        } catch (Exception e) {
            System.err.println(">>> Could not fetch patient email: " + e.getMessage());
        }

        // trigger notification
        notify("APPOINTMENT", appointment.getPatientId(), patientEmail,
                "Your appointment has been rescheduled to " + dto.getNewAppointmentDate() + ".");

        // AUDIT LOG
        audit("RESCHEDULE_APPOINTMENT", appointment.getAppointmentCode(),
              "Rescheduled to: " + dto.getNewAppointmentDate());

        return mapToResponse(appointment);
    }

    @Override
    public AppointmentResponseDto completeAppointmentById(Long appointmentId) {
        Appointment appointment = repository.findById(appointmentId)
                .orElseThrow(() ->
                        new AppointmentNotFoundException("Appointment not found"));

        if (appointment.getStatus() != AppointmentStatus.BOOKED) {
            throw new IllegalStateException(
                    "Only BOOKED appointments can be completed");
        }

        appointment.setStatus(AppointmentStatus.COMPLETED);
        repository.save(appointment);

        // AUDIT LOG
        audit("COMPLETE_APPOINTMENT", appointment.getAppointmentCode(),
              "Appointment completed");

        return mapToResponse(appointment);
    }
    /*@Override
    public AppointmentResponseDto completeAppointmentByCode(String appointmentCode) {
        Appointment appointment = repository.findByAppointmentCode(appointmentCode)
                .orElseThrow(() -> new AppointmentNotFoundException(appointmentCode));

        if (appointment.getStatus() != AppointmentStatus.BOOKED) {
            throw new IllegalStateException("Only BOOKED appointments can be completed");
        }

        appointment.setStatus(AppointmentStatus.COMPLETED);
        repository.save(appointment);

        audit("COMPLETE_APPOINTMENT", appointment.getAppointmentCode(),
              "Appointment completed");

        return mapToResponse(appointment);
    }*/

    @Override
    public Long getTotalAppointments() {
        return repository.count();
    }

    @Override
    public Long getAppointmentCountByDate(String date) {
        LocalDate localDate = LocalDate.parse(date);
        return repository.countByAppointmentDate(localDate);
    }
    
    @Override
    public AppointmentResponseDto getAppointmentByCode(String appointmentCode) {
        Appointment appointment = repository.findByAppointmentCode(appointmentCode)
                .orElseThrow(() -> new AppointmentNotFoundException(appointmentCode));
        return mapToResponse(appointment);
    }

    // Audit helper
    private void audit(String action, String resourceId, String details) {
        try {
            auditClient.log(Map.of(
                "module",      "APPOINTMENT",
                "action",      action,
                "performedBy", "system",
                "resourceId",  resourceId != null ? resourceId : "",
                "details",     details != null ? details : ""
            ));
        } catch (Exception e) {
            System.err.println(">>> AUDIT FAILED [APPOINTMENT]: " + e.getMessage());
        }
    }
    
    // Notification helper
    private void notify(String type, Long patientId, String email, String message) {
        try {
            notificationClient.sendNotification(
                NotificationRequestDto.builder()
                    .recipientId(patientId)
                    .recipientType("PATIENT")
                    .notificationType(type)
                    .message(message)
                    .recipientEmail(email)
                    .build()
            );
        } catch (Exception e) {
            System.err.println(">>> NOTIFICATION FAILED [APPOINTMENT]: " + e.getMessage());
        }
    }
    
    // Appointment code generation helper
    private String generateAppointmentCode() {
        String date   = LocalDate.now().format(DateTimeFormatter.ofPattern("MMdd"));
        String suffix = UUID.randomUUID().toString().substring(0, 4).toUpperCase();
        return "APPT-" + date + "-" + suffix;
    }
    
    private AppointmentResponseDto mapToResponse(Appointment appointment) {
        return AppointmentResponseDto.builder()
                .id(appointment.getId())
                .appointmentCode(appointment.getAppointmentCode())
                .patientId(appointment.getPatientId())
                .patientCode(appointment.getPatientCode())
                .doctorCode(appointment.getDoctorCode())
                .appointmentDate(appointment.getAppointmentDate())
                .status(appointment.getStatus().name())
                .build();
    }
}