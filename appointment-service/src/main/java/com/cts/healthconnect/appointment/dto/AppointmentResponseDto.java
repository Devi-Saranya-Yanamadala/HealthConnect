package com.cts.healthconnect.appointment.dto;



import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class AppointmentResponseDto {

	private Long   id;
    private String appointmentCode;
    private Long patientId;
    private String patientCode;
    private String doctorCode;
    private LocalDate appointmentDate;
    private String status;
}
