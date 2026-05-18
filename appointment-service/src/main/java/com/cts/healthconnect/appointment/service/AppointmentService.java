package com.cts.healthconnect.appointment.service;



import java.time.LocalDate;
import java.util.List;

import com.cts.healthconnect.appointment.dto.*;

public interface AppointmentService {

    AppointmentResponseDto bookAppointment(AppointmentRequestDto dto);

    AppointmentResponseDto cancelAppointmentById(Long appointmentId);
    //AppointmentResponseDto cancelAppointmentByCode(String appointmentCode);

    AppointmentResponseDto rescheduleAppointment(AppointmentRescheduleRequestDto dto);

    Long getTotalAppointments();

	AppointmentResponseDto completeAppointmentById(Long appointmentId);
    //AppointmentResponseDto completeAppointmentByCode(String appointmentCode);
	
	Long getAppointmentCountByDate(String date);
	
	AppointmentResponseDto getAppointmentByCode(String appointmentCode);
	
	List<AppointmentResponseDto> getAppointmentsByPatientCode(String patientCode);
}


