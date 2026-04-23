package com.cts.healthconnect.appointment.controller;



import com.cts.healthconnect.appointment.dto.*;
import com.cts.healthconnect.appointment.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService service;

    @PostMapping
    public AppointmentResponseDto book(@RequestBody AppointmentRequestDto dto) {
        return service.bookAppointment(dto);
    }

    @PutMapping("/cancel/{appointmentCode}")
    public void cancel(@PathVariable String appointmentCode) {
        service.cancelAppointment(appointmentCode);
    }

    @PutMapping("/reschedule")
    public AppointmentResponseDto reschedule(
            @RequestBody AppointmentRescheduleRequestDto dto) {
        return service.rescheduleAppointment(dto);
    }

    @PutMapping("/complete/{appointmentCode}")
    public void complete(@PathVariable String appointmentCode) {
        service.completeAppointment(appointmentCode);
    }
}
