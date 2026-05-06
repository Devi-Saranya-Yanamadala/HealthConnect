package com.cts.healthconnect.billing.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class InsuranceClaimRequestDto {
    @NotBlank(message = "Appointment code is required")
    private String appointmentCode;

    @NotNull(message = "Patient ID is required")
    private Long patientId;

    @NotBlank(message = "Insurance provider is required")
    private String insuranceProvider;

    @NotBlank(message = "Policy number is required")
    private String policyNumber;

    @NotNull(message = "Claim amount is required")
    @Positive(message = "Claim amount must be positive")
    private Double claimAmount;
}