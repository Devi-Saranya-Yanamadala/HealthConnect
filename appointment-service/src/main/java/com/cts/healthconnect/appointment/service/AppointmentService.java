package com.cts.healthconnect.appointment.service;



import com.cts.healthconnect.appointment.dto.*;

public interface AppointmentService {

    AppointmentResponseDto bookAppointment(AppointmentRequestDto dto);

    AppointmentResponseDto cancelAppointmentById(Long appointmentId);

    AppointmentResponseDto rescheduleAppointment(AppointmentRescheduleRequestDto dto);


    Long getTotalAppointments();

	AppointmentResponseDto completeAppointmentById(Long appointmentId);
}


