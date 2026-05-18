package com.cts.healthconnect.billing.client;

import com.cts.healthconnect.billing.dto.PatientResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "patient-service", url = "http://localhost:5003")
public interface PatientClient {

    @GetMapping("/api/patients/by-id/{id}")
    PatientResponseDto getPatientById(@PathVariable("id") Long id);
}