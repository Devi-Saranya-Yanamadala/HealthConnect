package com.cts.healthconnect.billing.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.cts.healthconnect.billing.dto.InsuranceClaimRequestDto;
import com.cts.healthconnect.billing.dto.InsuranceClaimResponseDto;
import com.cts.healthconnect.billing.entity.ClaimStatus;
import com.cts.healthconnect.billing.entity.InsuranceClaim;
import com.cts.healthconnect.billing.exception.ClaimNotFoundException;
import com.cts.healthconnect.billing.repository.InsuranceClaimRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class InsuranceClaimServiceImpl implements InsuranceClaimService {

    private final InsuranceClaimRepository repository;

    @Override
    public InsuranceClaimResponseDto submitClaim(InsuranceClaimRequestDto dto) {

        InsuranceClaim claim = InsuranceClaim.builder()
                .claimNumber(UUID.randomUUID().toString())
                .appointmentCode(dto.getAppointmentCode())
                .patientId(dto.getPatientId())
                .insuranceProvider(dto.getInsuranceProvider())
                .policyNumber(dto.getPolicyNumber())
                .claimAmount(dto.getClaimAmount())
                .status(ClaimStatus.SUBMITTED)
                .build();

        repository.save(claim);
        return map(claim);
    }

    @Override
    public InsuranceClaimResponseDto getClaim(String claimNumber) {

        InsuranceClaim claim = repository.findByClaimNumber(claimNumber)
                .orElseThrow(() -> new ClaimNotFoundException(claimNumber));

        return map(claim);
    }

    @Override
    public void approveClaim(String claimNumber, Double approvedAmount) {

        InsuranceClaim claim = repository.findByClaimNumber(claimNumber)
                .orElseThrow(() -> new ClaimNotFoundException(claimNumber));

        claim.setApprovedAmount(approvedAmount);
        claim.setStatus(ClaimStatus.APPROVED);
    }

    @Override
    public void rejectClaim(String claimNumber) {

        InsuranceClaim claim = repository.findByClaimNumber(claimNumber)
                .orElseThrow(() -> new ClaimNotFoundException(claimNumber));

        claim.setStatus(ClaimStatus.REJECTED);
    }

    @Override
    public void settleClaim(String claimNumber) {

        InsuranceClaim claim = repository.findByClaimNumber(claimNumber)
                .orElseThrow(() -> new ClaimNotFoundException(claimNumber));

        claim.setStatus(ClaimStatus.SETTLED);
    }

    private InsuranceClaimResponseDto map(InsuranceClaim claim) {
        return InsuranceClaimResponseDto.builder()
                .claimNumber(claim.getClaimNumber())
                .appointmentCode(claim.getAppointmentCode())
                .claimAmount(claim.getClaimAmount())
                .approvedAmount(claim.getApprovedAmount())
                .status(claim.getStatus().name())
                .build();
    }
}
