package com.cts.healthconnect.patient.dto;



import com.cts.healthconnect.patient.entity.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PatientRequestDto {

    private String patientCode;
    private String fullName;
    private Gender gender;
    private LocalDate dob;
    private String phone;
    private String email;
    private BloodGroup bloodGroup;
    private String address;

    private String emergencyContactName;
    private String emergencyContactPhone;
    private String nationalId;
}

