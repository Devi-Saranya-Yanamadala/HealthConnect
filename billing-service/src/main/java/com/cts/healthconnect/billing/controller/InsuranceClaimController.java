package com.cts.healthconnect.billing.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cts.healthconnect.billing.dto.InsuranceClaimRequestDto;
import com.cts.healthconnect.billing.dto.InsuranceClaimResponseDto;
import com.cts.healthconnect.billing.service.InsuranceClaimService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/claims")
@RequiredArgsConstructor
public class InsuranceClaimController {

    private final InsuranceClaimService service;

    @PostMapping
    public InsuranceClaimResponseDto submit(@RequestBody InsuranceClaimRequestDto dto) {
        return service.submitClaim(dto);
    }

    @GetMapping("/{claimNumber}")
    public InsuranceClaimResponseDto get(@PathVariable String claimNumber) {
        return service.getClaim(claimNumber);
    }

    @PutMapping("/{claimNumber}/approve")
    public void approve(@PathVariable String claimNumber,
                        @RequestParam Double approvedAmount) {
        service.approveClaim(claimNumber, approvedAmount);
    }

    @PutMapping("/{claimNumber}/reject")
    public void reject(@PathVariable String claimNumber) {
        service.rejectClaim(claimNumber);
    }

    @PutMapping("/{claimNumber}/settle")
    public void settle(@PathVariable String claimNumber) {
        service.settleClaim(claimNumber);
    }
}
