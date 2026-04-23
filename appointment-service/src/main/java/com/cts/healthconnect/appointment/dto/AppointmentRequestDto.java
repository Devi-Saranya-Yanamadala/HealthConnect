package com.cts.healthconnect.appointment.dto;



import lombok.Data;

import java.time.LocalDate;

@Data
public class AppointmentRequestDto {

    private Long patientId;
    private String doctorCode;
    private Long slotId;
    private LocalDate appointmentDate;
}
