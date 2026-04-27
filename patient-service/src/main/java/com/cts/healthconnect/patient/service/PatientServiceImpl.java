package com.cts.healthconnect.patient.service;

import com.cts.healthconnect.patient.dto.*;
import com.cts.healthconnect.patient.entity.*;
import com.cts.healthconnect.patient.exception.PatientNotFoundException;
import com.cts.healthconnect.patient.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional   // ✅ THIS FIXES YOUR ISSUE
public class PatientServiceImpl implements PatientService {

    private final PatientRepository repository;

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
        return map(patient);
    }

    @Override
    public PatientResponseDto getPatientByCode(String patientCode) {

        Patient patient = repository.findByPatientCode(patientCode)
                .orElseThrow(() -> new PatientNotFoundException(patientCode));

        return map(patient);
    }

    @Override
    public void deactivatePatient(String patientCode) {

        Patient patient = repository.findByPatientCode(patientCode)
                .orElseThrow(() -> new PatientNotFoundException(patientCode));

        patient.setActive(false);
        patient.setStatus(PatientStatus.INACTIVE);
        // ✅ No save() needed — transaction handles it
    }

    @Override
    public PatientResponseDto markPatientDeceased(String patientCode) {

        Patient patient = repository.findByPatientCode(patientCode)
                .orElseThrow(() -> new PatientNotFoundException(patientCode));

        patient.setStatus(PatientStatus.DECEASED);
        patient.setActive(false);

        return map(patient);
    }
    

    @Override
    public Long getTotalPatients() {
        return repository.count();
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