package com.cts.healthconnect.appointment.dto;



import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class AppointmentResponseDto {

    private String appointmentCode;
    private Long patientId;
    private String doctorCode;
    private LocalDate appointmentDate;
    private String status;
}
