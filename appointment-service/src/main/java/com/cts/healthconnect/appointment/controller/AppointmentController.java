package com.cts.healthconnect.appointment.controller;

import com.cts.healthconnect.appointment.dto.*;
import com.cts.healthconnect.appointment.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService service;

    // ✅ Book appointment
    @PostMapping
    public AppointmentResponseDto book(@RequestBody AppointmentRequestDto dto) {
        return service.bookAppointment(dto);
    }

    // ✅ Cancel appointment (PATCH)
    @PatchMapping("/{appointmentId}/cancel")
    public ResponseEntity<AppointmentResponseDto> cancelAppointment(
            @PathVariable Long appointmentId) {

        return ResponseEntity.ok(
                service.cancelAppointmentById(appointmentId)
        );
    }

    // ✅ Reschedule appointment
    @PutMapping("/reschedule")
    public AppointmentResponseDto reschedule(
            @RequestBody AppointmentRescheduleRequestDto dto) {
        return service.rescheduleAppointment(dto);
    }

    // ✅ COMPLETE appointment (PATCH) — FIXED ✅
    @PatchMapping("/{appointmentId}/complete")
    public ResponseEntity<AppointmentResponseDto> completeAppointment(
            @PathVariable Long appointmentId) {

        return ResponseEntity.ok(
                service.completeAppointmentById(appointmentId)
        );
    }

    // ✅ Get total appointment count
    @GetMapping("/count")
    public Long getTotalAppointments() {
        return service.getTotalAppointments();
    }
    
 // ✅ ADDED: count appointments on a specific date
    @GetMapping("/count/by-date")
    public Long getAppointmentsByDate(@RequestParam String date) {
        return service.getAppointmentCountByDate(date);
    }
}
