package com.cts.healthconnect.analytics.controller;

import com.cts.healthconnect.analytics.dto.AnalyticsResponseDto;
import com.cts.healthconnect.analytics.dto.KpiResponseDto;
import com.cts.healthconnect.analytics.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
public class AnalyticsController {
    private final AnalyticsService service;

    @GetMapping("/dashboard")
    public AnalyticsResponseDto dashboard() {
        return service.getDashboardMetrics();
    }

    @GetMapping("/kpis")
    public KpiResponseDto getKpis() {
        return service.getKpis();
    }
}