package com.cts.healthconnect.analytics.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "patient-service")
public interface PatientClient {

    @GetMapping("/api/patients/count")
    Long getTotalPatients();

    // ✅ ADDED
    @GetMapping("/api/patients/count/by-date")
    Long getPatientsByDate(@RequestParam("date") String date);
}