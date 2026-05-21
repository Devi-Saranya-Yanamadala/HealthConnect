package com.cts.healthconnect.ward.service;

import com.cts.healthconnect.ward.client.AuditClient;
import com.cts.healthconnect.ward.client.NotificationClient;
import com.cts.healthconnect.ward.client.PatientClient;
import com.cts.healthconnect.ward.dto.*;
import com.cts.healthconnect.ward.entity.*;
import com.cts.healthconnect.ward.exception.*;
import com.cts.healthconnect.ward.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class WardServiceImpl implements WardService {

    private final WardAdmissionRepository admissionRepository;
    private final BedRepository           bedRepository;
    private final AuditClient             auditClient;
    private final NotificationClient      notificationClient;
    private final PatientClient           patientClient;   

    @Override
    public WardAdmissionResponseDto admitPatient(WardAdmissionRequestDto dto) {

        Bed bed = bedRepository.findByBedNumber(dto.getBedNumber())
                .orElseThrow(() -> new BedNotAvailableException(dto.getBedNumber()));

        if (Boolean.TRUE.equals(bed.getOccupied())) {
            throw new BedNotAvailableException(dto.getBedNumber());
        }

        bed.setOccupied(true);

        WardAdmission admission = WardAdmission.builder()
        		.admissionCode(generateAdmissionCode())
                .patientCode(dto.getPatientCode())
                .doctorCode(dto.getDoctorCode())
                .wardType(dto.getWardType())
                .bedNumber(dto.getBedNumber())
                .status(AdmissionStatus.ADMITTED)
                .build();

        admissionRepository.save(admission);
        
        // fetch patient email
        String patientEmail = null;
        try {
            patientEmail = patientClient.getPatient(dto.getPatientCode()).getEmail();
        } catch (Exception e) {
            System.err.println(">>> Could not fetch patient email: " + e.getMessage());
        }

        notify("ADMISSION", dto.getPatientCode(), patientEmail,
               "You have been admitted to " + dto.getWardType()
               + " ward, Bed: " + dto.getBedNumber() + ".");


        // Audit log
        audit("ADMIT_PATIENT", admission.getAdmissionCode(),
              "Patient: " + dto.getPatientCode()
              + " | Ward: " + dto.getWardType()
              + " | Bed: " + dto.getBedNumber());

        return map(admission);
    }
    
    private String generateAdmissionCode() {
        return admissionRepository.findLastAdmissionCode()
            .map(last -> {
                // last is like "ADM-007", extract number after "ADM-"
                int num = Integer.parseInt(last.substring(4));
                return String.format("ADM-%03d", num + 1);
            })
            .orElse("ADM-001");
    }
    
    @Override
    public WardAdmissionResponseDto getAdmission(String admissionCode) {
        WardAdmission admission = admissionRepository.findByAdmissionCode(admissionCode)
                .orElseThrow(() -> new AdmissionNotFoundException(admissionCode));
        return map(admission);
    }

    @Override
    public void dischargePatient(String admissionCode) {

        WardAdmission admission = admissionRepository.findByAdmissionCode(admissionCode)
                .orElseThrow(() -> new AdmissionNotFoundException(admissionCode));

        admission.setStatus(AdmissionStatus.DISCHARGED);
        admission.setDischargedAt(LocalDateTime.now());

        Bed bed = bedRepository.findByBedNumber(admission.getBedNumber())
                .orElseThrow();
        bed.setOccupied(false);
        
        // fetch patient email
        String patientEmail = null;
        try {
            patientEmail = patientClient.getPatient(admission.getPatientCode()).getEmail();
        } catch (Exception e) {
            System.err.println(">>> Could not fetch patient email: " + e.getMessage());
        }

        notify("ADMISSION", admission.getPatientCode(), patientEmail,
               "You have been discharged from HealthConnect Hospital. We wish you a speedy recovery.");

        // Audit log
        audit("DISCHARGE_PATIENT", admissionCode,
              "Patient: " + admission.getPatientCode() + " discharged");
    }

    @Override
    public Long getTotalAdmissions() {
        return admissionRepository.count();
    }

    @Override
    public Long getActiveAdmissions() {
        return admissionRepository.countByStatus(AdmissionStatus.ADMITTED);
    }
    
    private void notify(String type, String patientCode, String email, String message) {
        try {
            notificationClient.sendNotification(
                NotificationRequestDto.builder()
                    .recipientType("PATIENT")
                    .notificationType(type)
                    .message(message)
                    .recipientEmail(email)
                    .build()
            );
        } catch (Exception e) {
            System.err.println(">>> NOTIFICATION FAILED [WARD]: " + e.getMessage());
        }
    }

    // ADDED: count admissions on a specific date
    @Override
    public Long getAdmissionCountByDate(String date) {
        LocalDate localDate = LocalDate.parse(date);
        LocalDateTime start = localDate.atStartOfDay();
        LocalDateTime end   = localDate.atTime(23, 59, 59);
        return admissionRepository.countByAdmittedAtBetween(start, end);
    }

    // ADDED: count active admissions on a specific date
    @Override
    public Long getActiveAdmissionCountByDate(String date) {
        LocalDate localDate = LocalDate.parse(date);
        LocalDateTime start = localDate.atStartOfDay();
        LocalDateTime end   = localDate.atTime(23, 59, 59);
        return admissionRepository.countByStatusAndAdmittedAtBetween(
            AdmissionStatus.ADMITTED, start, end
        );
    }

    // Audit helper — never throws, silent on failure
    private void audit(String action, String resourceId, String details) {
        try {
            auditClient.log(Map.of(
                "module",      "WARD",
                "action",      action,
                "performedBy", "system",
                "resourceId",  resourceId != null ? resourceId : "",
                "details",     details    != null ? details    : ""
            ));
        } catch (Exception e) {
            System.err.println(">>> AUDIT FAILED [WARD]: " + e.getMessage());
        }
    }

    private WardAdmissionResponseDto map(WardAdmission a) {
        return WardAdmissionResponseDto.builder()
                .admissionCode(a.getAdmissionCode())
                .patientCode(a.getPatientCode())
                .doctorCode(a.getDoctorCode())
                .wardType(a.getWardType().name())
                .bedNumber(a.getBedNumber())
                .status(a.getStatus().name())
                .build();
    }
}