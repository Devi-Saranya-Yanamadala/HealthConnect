package com.cts.healthconnect.analytics.service;

import com.cts.healthconnect.analytics.dto.AnalyticsResponseDto;
import com.cts.healthconnect.analytics.dto.KpiResponseDto;

public interface AnalyticsService {

    AnalyticsResponseDto getDashboardMetrics();
    
    KpiResponseDto getKpis();
}