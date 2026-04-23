package com.cts.healthconnect.patient.dto;


import com.cts.healthconnect.patient.entity.*;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PatientResponseDto {

    private Long patient_id;
	private String patientCode;
    private String fullName;
    private Gender gender;
    private String phone;
    private BloodGroup bloodGroup;
    private PatientStatus status;
    private Boolean active;
}

