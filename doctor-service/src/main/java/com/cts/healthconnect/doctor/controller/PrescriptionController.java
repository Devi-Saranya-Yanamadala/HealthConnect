package com.cts.healthconnect.doctor.controller;

import com.cts.healthconnect.doctor.dto.PrescriptionRequestDto;
import com.cts.healthconnect.doctor.dto.PrescriptionResponseDto;
import com.cts.healthconnect.doctor.service.PrescriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors/prescriptions")
@RequiredArgsConstructor
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    // Create prescription
    @PostMapping
    public ResponseEntity<PrescriptionResponseDto> create(
            @Valid @RequestBody PrescriptionRequestDto dto) {
        return ResponseEntity.ok(prescriptionService.createPrescription(dto));
    }

    // Get by prescription code
    @GetMapping("/{code}")
    public ResponseEntity<PrescriptionResponseDto> getByCode(
            @PathVariable String code) {
        return ResponseEntity.ok(prescriptionService.getByPrescriptionCode(code));
    }

    // Get by appointment code
    @GetMapping("/appointment/{appointmentCode}")
    public ResponseEntity<PrescriptionResponseDto> getByAppointment(
            @PathVariable String appointmentCode) {
        return ResponseEntity.ok(prescriptionService.getByAppointmentCode(appointmentCode));
    }

    // Get all prescriptions for a patient
    @GetMapping("/patient/{patientCode}")
    public ResponseEntity<List<PrescriptionResponseDto>> getByPatient(
            @PathVariable String patientCode) {
        return ResponseEntity.ok(prescriptionService.getByPatientCode(patientCode));
    }

    // Get all prescriptions written by a doctor
    @GetMapping("/doctor/{doctorCode}")
    public ResponseEntity<List<PrescriptionResponseDto>> getByDoctor(
            @PathVariable String doctorCode) {
        return ResponseEntity.ok(prescriptionService.getByDoctorCode(doctorCode));
    }
}