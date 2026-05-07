package com.cts.healthconnect.ward.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "audit-compliance")
public interface AuditClient {

    @PostMapping("/api/audit/logs")
    void log(@RequestBody Map<String, String> log);
}