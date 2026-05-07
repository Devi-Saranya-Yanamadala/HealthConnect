package com.cts.healthconnect.analytics.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "ward-service")
public interface WardClient {

    @GetMapping("/api/wards/admissions/count")
    Long getTotalAdmissions();

    @GetMapping("/api/wards/admissions/active")
    Long getActiveAdmissions();

    // ✅ ADDED
    @GetMapping("/api/wards/admissions/count/by-date")
    Long getAdmissionsByDate(@RequestParam("date") String date);

    // ✅ ADDED
    @GetMapping("/api/wards/admissions/active/by-date")
    Long getActiveAdmissionsByDate(@RequestParam("date") String date);
}