package com.cts.healthconnect.billing.dto;

import lombok.Data;

@Data
public class PatientResponseDto {
    private Long   patient_id;
    private String patientCode;
    private String fullName;
    private String email;
}