package com.cts.healthconnect.patient.dto;

import com.cts.healthconnect.patient.entity.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PatientRequestDto {

    @NotBlank(message = "Patient code must not be blank")
    @Size(min = 3, max = 20, message = "Patient code must be between 3 and 20 characters")
    private String patientCode;

    @NotBlank(message = "Full name must not be blank")
    @Size(min = 3, max = 100, message = "Full name must be between 3 and 100 characters")
    private String fullName;

    @NotNull(message = "Gender must be provided")
    private Gender gender;

    @NotNull(message = "Date of birth must be provided")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dob;

    @NotBlank(message = "Phone number must not be blank")
    @Pattern(
        regexp = "^[6-9][0-9]{9}$",
        message = "Phone number must be a valid 10-digit Indian mobile number"
    )
    private String phone;

    @Email(message = "Email must be valid")
    private String email;

    @NotNull(message = "Blood group must be provided")
    private BloodGroup bloodGroup;

    @NotBlank(message = "Address must not be blank")
    @Size(max = 255, message = "Address must not exceed 255 characters")
    private String address;

    @NotBlank(message = "Emergency contact name must not be blank")
    private String emergencyContactName;

    @NotBlank(message = "Emergency contact phone must not be blank")
    @Pattern(
        regexp = "^[6-9][0-9]{9}$",
        message = "Emergency contact phone must be valid"
    )
    private String emergencyContactPhone;

    @NotBlank(message = "National ID must not be blank")
    @Size(min = 6, max = 20, message = "National ID must be valid")
    private String nationalId;
}