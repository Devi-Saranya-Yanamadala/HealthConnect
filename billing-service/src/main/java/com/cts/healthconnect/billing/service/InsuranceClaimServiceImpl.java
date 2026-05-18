package com.cts.healthconnect.billing.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.cts.healthconnect.billing.client.NotificationClient;
import com.cts.healthconnect.billing.client.PatientClient;
import com.cts.healthconnect.billing.dto.InsuranceClaimRequestDto;
import com.cts.healthconnect.billing.dto.InsuranceClaimResponseDto;
import com.cts.healthconnect.billing.dto.NotificationRequestDto;
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
    private final NotificationClient       notificationClient;
    private final PatientClient            patientClient;

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
        
        // notify
        String patientEmail = null;
        try {
            patientEmail = patientClient.getPatientById(dto.getPatientId()).getEmail();
        } catch (Exception e) {
            System.err.println(">>> Could not fetch patient email: " + e.getMessage());
        }

        notify(dto.getPatientId(), patientEmail,
               "Your insurance claim has been submitted successfully. Claim number: "
               + claim.getClaimNumber() + ".");
        
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
        
        // notify
        String patientEmail = null;
        try {
            patientEmail = patientClient.getPatientById(claim.getPatientId()).getEmail();
        } catch (Exception e) {
            System.err.println(">>> Could not fetch patient email: " + e.getMessage());
        }

        notify(claim.getPatientId(), patientEmail,
               "Your insurance claim " + claimNumber
               + " has been approved. Approved amount: ₹" + approvedAmount + ".");
    }

    @Override
    public void rejectClaim(String claimNumber) {

        InsuranceClaim claim = repository.findByClaimNumber(claimNumber)
                .orElseThrow(() -> new ClaimNotFoundException(claimNumber));

        claim.setStatus(ClaimStatus.REJECTED);
        
        // notify
        String patientEmail = null;
        try {
            patientEmail = patientClient.getPatientById(claim.getPatientId()).getEmail();
        } catch (Exception e) {
            System.err.println(">>> Could not fetch patient email: " + e.getMessage());
        }

        notify(claim.getPatientId(), patientEmail,
               "Your insurance claim " + claimNumber
               + " has been rejected. Please contact support for more details.");
    }

    @Override
    public void settleClaim(String claimNumber) {

        InsuranceClaim claim = repository.findByClaimNumber(claimNumber)
                .orElseThrow(() -> new ClaimNotFoundException(claimNumber));

        claim.setStatus(ClaimStatus.SETTLED);
        
        // notify
        String patientEmail = null;
        try {
            patientEmail = patientClient.getPatientById(claim.getPatientId()).getEmail();
        } catch (Exception e) {
            System.err.println(">>> Could not fetch patient email: " + e.getMessage());
        }

        notify(claim.getPatientId(), patientEmail,
               "Your insurance claim " + claimNumber
               + " has been settled. Amount: ₹" + claim.getApprovedAmount() + ".");
        
        
    }
    
    
    // notify helper
    private void notify(Long patientId, String email, String message) {
        try {
            notificationClient.sendNotification(
                NotificationRequestDto.builder()
                    .recipientId(patientId)
                    .recipientType("PATIENT")
                    .notificationType("BILLING")
                    .message(message)
                    .recipientEmail(email)
                    .build()
            );
        } catch (Exception e) {
            System.err.println(">>> NOTIFICATION FAILED [BILLING]: " + e.getMessage());
        }
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
