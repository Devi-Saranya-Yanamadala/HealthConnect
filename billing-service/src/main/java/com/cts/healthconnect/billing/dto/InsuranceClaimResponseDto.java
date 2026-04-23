package com.cts.healthconnect.billing.dto;



import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InsuranceClaimResponseDto {

    private String claimNumber;
    private String appointmentCode;
    private Double claimAmount;
    private Double approvedAmount;
    private String status;
}

