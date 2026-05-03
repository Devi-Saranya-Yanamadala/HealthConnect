package com.cts.healthconnect.ward.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.cts.healthconnect.ward.dto.PatientResponseDto;

@FeignClient(name = "patient-service", url = "http://localhost:5003")
public interface PatientClient {

    @GetMapping("/api/patients/{patientCode}")
    PatientResponseDto getPatient(@PathVariable String patientCode);
}