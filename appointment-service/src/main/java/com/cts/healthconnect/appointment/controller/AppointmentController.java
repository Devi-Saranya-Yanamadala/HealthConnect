package com.cts.healthconnect.appointment.controller;

import com.cts.healthconnect.appointment.dto.*;
import com.cts.healthconnect.appointment.service.AppointmentService;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService service;

    // Book appointment
    @PostMapping
    public AppointmentResponseDto book(@RequestBody AppointmentRequestDto dto) {
        return service.bookAppointment(dto);
    }

    // Cancel appointment (PATCH)
    @PatchMapping("/{appointmentId}/cancel")
    public ResponseEntity<AppointmentResponseDto> cancelAppointment(
            @PathVariable Long appointmentId) {

        return ResponseEntity.ok(
                service.cancelAppointmentById(appointmentId)
        );
    }
    @PatchMapping("/code/{appointmentCode}/cancel")
    public ResponseEntity<AppointmentResponseDto> cancelByCode(
            @PathVariable String appointmentCode) {
        return ResponseEntity.ok(service.cancelAppointmentByCode(appointmentCode));
    }

    // Reschedule appointment
    @PutMapping("/reschedule")
    public AppointmentResponseDto reschedule(
            @RequestBody AppointmentRescheduleRequestDto dto) {
        return service.rescheduleAppointment(dto);
    }

    // COMPLETE appointment (PATCH) — FIXED ✅
    @PatchMapping("/{appointmentId}/complete")
    public ResponseEntity<AppointmentResponseDto> completeAppointment(
            @PathVariable Long appointmentId) {

        return ResponseEntity.ok(
                service.completeAppointmentById(appointmentId)
        );
    }
    @PatchMapping("/code/{appointmentCode}/complete")
    public ResponseEntity<AppointmentResponseDto> completeByCode(
            @PathVariable String appointmentCode) {
        return ResponseEntity.ok(service.completeAppointmentByCode(appointmentCode));
    }

    // Get total appointment count
    @GetMapping("/count")
    public Long getTotalAppointments() {
        return service.getTotalAppointments();
    }
    
    // count appointments on a specific date
    @GetMapping("/count/by-date")
    public Long getAppointmentsByDate(@RequestParam String date) {
        return service.getAppointmentCountByDate(date);
    }
    
    @GetMapping("/by-code/{appointmentCode}")
    public AppointmentResponseDto getByCode(@PathVariable String appointmentCode) {
        return service.getAppointmentByCode(appointmentCode);
    }
    
    // list of appointments of a particular patient
    @GetMapping("/patient/{patientCode}")
    public List<AppointmentResponseDto> getByPatientCode(
            @PathVariable String patientCode) {
        return service.getAppointmentsByPatientCode(patientCode);
    }
    
    @GetMapping("/doctor/{doctorCode}")
    public List<AppointmentResponseDto> getByDoctorCode(
            @PathVariable String doctorCode) {
        return service.getAppointmentsByDoctorCode(doctorCode);
    }
    
 
}
