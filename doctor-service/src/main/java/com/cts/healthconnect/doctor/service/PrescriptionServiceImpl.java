package com.cts.healthconnect.doctor.service;

import com.cts.healthconnect.doctor.dto.PrescriptionRequestDto;
import com.cts.healthconnect.doctor.dto.PrescriptionResponseDto;
import com.cts.healthconnect.doctor.entity.Prescription;
import com.cts.healthconnect.doctor.exception.PrescriptionNotFoundException;
import com.cts.healthconnect.doctor.repository.PrescriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PrescriptionServiceImpl implements PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;

    /* ── Auto-generate PRX-001, PRX-002 … ── */
    private String generatePrescriptionCode() {
        return prescriptionRepository.findLastPrescriptionCode()
            .map(last -> {
                int num = Integer.parseInt(last.substring(4)); // "PRX-007" → 7
                return String.format("PRX-%03d", num + 1);
            })
            .orElse("PRX-001");
    }

    @Override
    public PrescriptionResponseDto createPrescription(PrescriptionRequestDto dto) {
        Prescription p = Prescription.builder()
            .prescriptionCode(generatePrescriptionCode())
            .appointmentCode(dto.getAppointmentCode())
            .patientCode(dto.getPatientCode())
            .doctorCode(dto.getDoctorCode())
            .diagnosis(dto.getDiagnosis())
            .medicinesJson(dto.getMedicinesJson())
            .notes(dto.getNotes())
            .followUpDate(dto.getFollowUpDate())
            .build();

        return toDto(prescriptionRepository.save(p));
    }

    @Override
    public PrescriptionResponseDto getByPrescriptionCode(String code) {
        return prescriptionRepository.findByPrescriptionCode(code)
            .map(this::toDto)
            .orElseThrow(() -> new PrescriptionNotFoundException(
                "No prescription found with code: " + code));
    }

    @Override
    public PrescriptionResponseDto getByAppointmentCode(String appointmentCode) {
        return prescriptionRepository.findByAppointmentCode(appointmentCode)
            .map(this::toDto)
            .orElseThrow(() -> new PrescriptionNotFoundException(
                "No prescription found for appointment: " + appointmentCode));
    }

    @Override
    public List<PrescriptionResponseDto> getByPatientCode(String patientCode) {
        // Returns empty list — never throws, no 500
        return prescriptionRepository.findByPatientCode(patientCode)
            .stream().map(this::toDto).toList();
    }

    @Override
    public List<PrescriptionResponseDto> getByDoctorCode(String doctorCode) {
        return prescriptionRepository.findByDoctorCode(doctorCode)
            .stream().map(this::toDto).toList();
    }

    private PrescriptionResponseDto toDto(Prescription p) {
        return PrescriptionResponseDto.builder()
            .id(p.getId())
            .prescriptionCode(p.getPrescriptionCode())
            .appointmentCode(p.getAppointmentCode())
            .patientCode(p.getPatientCode())
            .doctorCode(p.getDoctorCode())
            .diagnosis(p.getDiagnosis())
            .medicinesJson(p.getMedicinesJson())
            .notes(p.getNotes())
            .followUpDate(p.getFollowUpDate())
            .createdAt(p.getCreatedAt())
            .build();
    }
}