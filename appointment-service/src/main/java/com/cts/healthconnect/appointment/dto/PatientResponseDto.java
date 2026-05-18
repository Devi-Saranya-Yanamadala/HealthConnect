package com.cts.healthconnect.appointment.dto;

import lombok.Data;

@Data
public class PatientResponseDto {
    private Long patient_id;
    private String patientCode;
    private String fullName;
    private String phone;
    private String email;
}