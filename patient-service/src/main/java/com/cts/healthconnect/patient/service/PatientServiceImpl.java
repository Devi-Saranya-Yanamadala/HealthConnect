package com.cts.healthconnect.patient.service;

import com.cts.healthconnect.patient.client.AuditClient;
import com.cts.healthconnect.patient.dto.*;
import com.cts.healthconnect.patient.entity.*;
import com.cts.healthconnect.patient.exception.PatientNotFoundException;
import com.cts.healthconnect.patient.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PatientServiceImpl implements PatientService {

    private final PatientRepository repository;
    private final AuditClient       auditClient;

    @Override
    public PatientResponseDto registerPatient(PatientRequestDto dto) {
        Patient patient = Patient.builder()
                .patientCode(dto.getPatientCode())
                .fullName(dto.getFullName())
                .gender(dto.getGender())
                .dob(dto.getDob())
                .phone(dto.getPhone())
                .email(dto.getEmail())
                .bloodGroup(dto.getBloodGroup())
                .address(dto.getAddress())
                .emergencyContactName(dto.getEmergencyContactName())
                .emergencyContactPhone(dto.getEmergencyContactPhone())
                .nationalId(dto.getNationalId())
                .status(PatientStatus.ACTIVE)
                .active(true)
                .build();

        repository.save(patient);

        // ✅ AUDIT LOG
        audit("REGISTER_PATIENT", patient.getPatientCode(),
              "Patient registered: " + dto.getFullName());

        return map(patient);
    }

    @Override
    public PatientResponseDto getPatientByCode(String patientCode) {
        Patient patient = repository.findByPatientCode(patientCode)
                .orElseThrow(() -> new PatientNotFoundException(patientCode));
        return map(patient);
    }

    @Override
    public List<PatientResponseDto> getAllPatients() {
        return repository.findAll()
                .stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    @Override
    public void activatePatient(String patientCode) {
        Patient patient = repository.findByPatientCode(patientCode)
                .orElseThrow(() -> new PatientNotFoundException(patientCode));
        patient.setActive(true);
        patient.setStatus(PatientStatus.ACTIVE);

        // ✅ AUDIT LOG
        audit("ACTIVATE_PATIENT", patientCode, "Patient activated");
    }

    @Override
    public void deactivatePatient(String patientCode) {
        Patient patient = repository.findByPatientCode(patientCode)
                .orElseThrow(() -> new PatientNotFoundException(patientCode));
        patient.setActive(false);
        patient.setStatus(PatientStatus.INACTIVE);

        // ✅ AUDIT LOG
        audit("DEACTIVATE_PATIENT", patientCode, "Patient deactivated");
    }

    @Override
    public PatientResponseDto markPatientDeceased(String patientCode) {
        Patient patient = repository.findByPatientCode(patientCode)
                .orElseThrow(() -> new PatientNotFoundException(patientCode));
        patient.setStatus(PatientStatus.DECEASED);
        patient.setActive(false);

        // ✅ AUDIT LOG
        audit("MARK_DECEASED", patientCode, "Patient marked as deceased");

        return map(patient);
    }

    @Override
    public Long getTotalPatients() {
        return repository.count();
    }

    @Override
    public Long getPatientCountByDate(String date) {
        LocalDate localDate = LocalDate.parse(date);
        LocalDateTime start = localDate.atStartOfDay();
        LocalDateTime end   = localDate.atTime(23, 59, 59);
        return repository.countByCreatedAtBetween(start, end);
    }

    // ✅ Audit helper — never throws, failure is silent
    private void audit(String action, String resourceId, String details) {
        try {
            auditClient.log(Map.of(
                "module",      "PATIENT",
                "action",      action,
                "performedBy", "system",
                "resourceId",  resourceId != null ? resourceId : "",
                "details",     details != null ? details : ""
            ));
        } catch (Exception e) {
            System.err.println(">>> AUDIT FAILED [PATIENT]: " + e.getMessage());
        }
    }

    private PatientResponseDto map(Patient p) {
        return PatientResponseDto.builder()
                .patient_id(p.getId())
                .patientCode(p.getPatientCode())
                .fullName(p.getFullName())
                .gender(p.getGender())
                .phone(p.getPhone())
                .bloodGroup(p.getBloodGroup())
                .status(p.getStatus())
                .active(p.getActive())
                .build();
    }
}