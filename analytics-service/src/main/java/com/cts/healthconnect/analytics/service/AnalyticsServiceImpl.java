package com.cts.healthconnect.analytics.service;

import com.cts.healthconnect.analytics.dto.*;
import com.cts.healthconnect.analytics.entity.HospitalMetrics;
import com.cts.healthconnect.analytics.repository.HospitalMetricsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnalyticsServiceImpl implements AnalyticsService {

    private final HospitalMetricsRepository repository;

    /**
     * Helper method to fetch the most recent metrics snapshot.
     * Uses Optional to avoid throwing raw RuntimeExceptions.
     */
    private Optional<HospitalMetrics> getLatestMetrics() {
        return repository.findAll(PageRequest.of(0, 1, Sort.by("updatedAt").descending()))
                .getContent()
                .stream()
                .findFirst();
    }

    @Override
    @Transactional(readOnly = true)
    public AnalyticsResponseDto getDashboardMetrics() {
        return getLatestMetrics()
                .map(this::mapToDashboardDto)
                .orElseGet(() -> {
                    log.warn("Dashboard requested but no metrics found in database.");
                    return createEmptyDashboardDto();
                });
    }

    @Override
    @Transactional(readOnly = true)
    public KpiResponseDto getKpis() {
        return getLatestMetrics()
                .map(this::mapToKpiDto)
                .orElseGet(() -> {
                    log.warn("KPIs requested but no metrics found in database.");
                    return createEmptyKpiDto();
                });
    }

    // --- Mapping Logic with Null Safety ---

    private AnalyticsResponseDto mapToDashboardDto(HospitalMetrics metrics) {
        return AnalyticsResponseDto.builder()
                .totalPatients(nullSafe(metrics.getTotalPatients()))
                .totalAppointments(nullSafe(metrics.getTotalAppointments()))
                .totalAdmissions(nullSafe(metrics.getTotalAdmissions()))
                .activeAdmissions(nullSafe(metrics.getActiveAdmissions()))
                .totalRevenue(nullSafe(metrics.getTotalRevenue()))
                .build();
    }

    private KpiResponseDto mapToKpiDto(HospitalMetrics metrics) {
        return KpiResponseDto.builder()
                .totalPatients(nullSafe(metrics.getTotalPatients()))
                .totalAppointments(nullSafe(metrics.getTotalAppointments()))
                .totalAdmissions(nullSafe(metrics.getTotalAdmissions()))
                .activeAdmissions(nullSafe(metrics.getActiveAdmissions()))
                .totalRevenue(nullSafe(metrics.getTotalRevenue()))
                .build();
    }

    // --- Default "Safe" Objects to prevent Frontend crashes ---

    private AnalyticsResponseDto createEmptyDashboardDto() {
        return AnalyticsResponseDto.builder()
                .totalPatients(0L).totalAppointments(0L)
                .totalAdmissions(0L).activeAdmissions(0L)
                .totalRevenue(0.0).build();
    }

    private KpiResponseDto createEmptyKpiDto() {
        return KpiResponseDto.builder()
                .totalPatients(0L).totalAppointments(0L)
                .totalAdmissions(0L).activeAdmissions(0L)
                .totalRevenue(0.0).build();
    }

    private Long nullSafe(Long value) { return value != null ? value : 0L; }
    private Double nullSafe(Double value) { return value != null ? value : 0.0; }
}