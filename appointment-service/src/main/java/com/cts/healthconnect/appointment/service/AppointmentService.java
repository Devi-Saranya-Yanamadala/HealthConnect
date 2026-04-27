package com.cts.healthconnect.appointment.service;



import com.cts.healthconnect.appointment.dto.*;

public interface AppointmentService {

    AppointmentResponseDto bookAppointment(AppointmentRequestDto dto);

    void cancelAppointment(String appointmentCode);

    AppointmentResponseDto rescheduleAppointment(AppointmentRescheduleRequestDto dto);

    void completeAppointment(String appointmentCode);
    
    Long getTotalAppointments();
}

