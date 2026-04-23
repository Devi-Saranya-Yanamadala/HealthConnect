package com.cts.healthconnect.billing.service;

import com.cts.healthconnect.billing.dto.InsuranceClaimRequestDto;
import com.cts.healthconnect.billing.dto.InsuranceClaimResponseDto;

public interface InsuranceClaimService {

    InsuranceClaimResponseDto submitClaim(InsuranceClaimRequestDto dto);

    InsuranceClaimResponseDto getClaim(String claimNumber);

    void approveClaim(String claimNumber, Double approvedAmount);

    void rejectClaim(String claimNumber);

    void settleClaim(String claimNumber);
}

