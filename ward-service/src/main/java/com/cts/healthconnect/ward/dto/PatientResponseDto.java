package com.cts.healthconnect.ward.dto;

import lombok.Data;

@Data
public class PatientResponseDto {
    private String patientCode;
    private Boolean active;
    private String status;
    private String email;
    private String fullName; 
}
