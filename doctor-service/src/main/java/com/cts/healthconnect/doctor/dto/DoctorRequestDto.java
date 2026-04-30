package com.cts.healthconnect.doctor.dto;

import java.time.LocalTime;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class DoctorRequestDto {

    @NotBlank(message = "Doctor code must not be blank")
    @Size(min = 3, max = 20, message = "Doctor code must be between 3 and 20 characters")
    private String doctorCode;

    @NotBlank(message = "Full name must not be blank")
    @Size(min = 3, max = 100, message = "Full name must be between 3 and 100 characters")
    private String fullName;

    @NotBlank(message = "Specialization must not be blank")
    private String specialization;

    @NotBlank(message = "Department must not be blank")
    private String department;

    @NotBlank(message = "License number must not be blank")
    @Size(min = 5, max = 30, message = "License number must be valid")
    private String licenseNumber;

    @NotBlank(message = "Phone number must not be blank")
    @Pattern(
        regexp = "^[6-9][0-9]{9}$",
        message = "Phone number must be a valid 10-digit Indian mobile number"
    )
    private String phoneNumber;

    @NotBlank(message = "Email must not be blank")
    @Email(message = "Email must be a valid email address")
    private String email;

    @NotNull(message = "Working start time must be provided")
    private LocalTime workingStartTime;

    @NotNull(message = "Working end time must be provided")
    private LocalTime workingEndTime;
}