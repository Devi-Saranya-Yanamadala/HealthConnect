package com.cts.healthconnect.billing.dto;

import lombok.Data;

@Data
public class AppointmentResponseDto {
    private String appointmentCode;
    private Long   patientId;
    private String doctorCode;
    private String appointmentDate;
    private String status;
}