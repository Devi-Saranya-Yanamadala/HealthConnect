package com.cts.healthconnect.doctor.dto;

import java.time.LocalTime;

import lombok.Data;

@Data
public class DoctorRequestDto {

    private String doctorCode;
    private String fullName;
    private String specialization;
    private String department;
    private String licenseNumber;
    private String phoneNumber;
    private String email;
    private LocalTime workingStartTime;
    private LocalTime workingEndTime;
}
