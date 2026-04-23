package com.cts.healthconnect.analytics.service;

import com.cts.healthconnect.analytics.dto.AnalyticsResponseDto;
import com.cts.healthconnect.analytics.entity.HospitalMetrics;
import com.cts.healthconnect.analytics.repository.HospitalMetricsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnalyticsServiceImpl implements AnalyticsService {

    private final HospitalMetricsRepository repository;

    @Override
    public AnalyticsResponseDto getDashboardMetrics() {

        HospitalMetrics latest = repository
                .findAll(PageRequest.of(0, 1, Sort.by("updatedAt").descending()))
                .getContent()
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No analytics data found"));

        return AnalyticsResponseDto.builder()
                .totalPatients(latest.getTotalPatients())
                .totalAppointments(latest.getTotalAppointments())
                .totalAdmissions(latest.getTotalAdmissions())
                .activeAdmissions(latest.getActiveAdmissions())
                .totalRevenue(latest.getTotalRevenue())
                .build();
    }
}