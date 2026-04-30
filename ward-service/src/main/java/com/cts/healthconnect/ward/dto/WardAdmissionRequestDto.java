package com.cts.healthconnect.ward.dto;

import com.cts.healthconnect.ward.entity.WardType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class WardAdmissionRequestDto {

    @NotNull(message = "Patient code must be provided")
    @Positive(message = "Patient code must be a positive number")
    private String patientCode;

    @NotBlank(message = "Doctor code must not be blank")
    private String doctorCode;

    @NotNull(message = "Ward type must be provided")
    private WardType wardType;

    @NotBlank(message = "Bed number must not be blank")
    private String bedNumber;
}