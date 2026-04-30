package com.cts.healthconnect.doctor.dto;

import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
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
	@Schema(description = "Doctor working start time")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
	private LocalTime workingStartTime;
	
	@Schema(description = "Doctor working end time")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
	private LocalTime workingEndTime;
}
