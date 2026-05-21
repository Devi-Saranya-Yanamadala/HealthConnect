package com.cts.healthconnect.doctor.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "patient-service")
public interface PatientClient {

    @GetMapping("/api/patients/{patientCode}")
    PatientInfo getPatientByCode(@PathVariable("patientCode") String patientCode);

    // Field names MUST match PatientResponseDto JSON keys exactly
    record PatientInfo(
        Long   patient_id,   // ← underscore — matches "patient_id" in JSON
        String patientCode,
        String fullName,
        String email,
        String phone
    ) {}
}