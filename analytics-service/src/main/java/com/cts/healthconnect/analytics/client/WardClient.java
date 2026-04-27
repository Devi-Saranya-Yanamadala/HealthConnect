package com.cts.healthconnect.analytics.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "ward-service")
public interface WardClient {

    @GetMapping("/api/wards/admissions/count")
    Long getTotalAdmissions();

    @GetMapping("/api/wards/admissions/active")
    Long getActiveAdmissions();
}
