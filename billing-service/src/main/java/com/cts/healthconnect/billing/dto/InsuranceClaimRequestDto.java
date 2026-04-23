package com.cts.healthconnect.billing.dto;



import lombok.Data;

@Data
public class InsuranceClaimRequestDto {

    private String appointmentCode;
    private Long patientId;
    private String insuranceProvider;
    private String policyNumber;
    private Double claimAmount;
}
