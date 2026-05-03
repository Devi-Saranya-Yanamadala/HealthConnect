package com.cts.healthconnect.analytics.service;

import com.cts.healthconnect.analytics.client.PatientClient;
import com.cts.healthconnect.analytics.entity.HospitalMetrics;
import com.cts.healthconnect.analytics.repository.HospitalMetricsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnalyticsAggregationServiceImpl implements AnalyticsAggregationService {

    private final PatientClient patientClient;
    private final HospitalMetricsRepository metricsRepository;

    // Runs every 5 minutes in production
    @Scheduled(fixedRate = 60000) 
    @Override
    public void aggregateMetrics() {
        log.info("Starting scheduled aggregation of hospital-wide metrics...");
        
        try {
            // 1. Fetching data from external Feign clients
            Long patientCount = patientClient.getTotalPatients();
            
            // 2. Build the snapshot entity
            HospitalMetrics latestMetrics = HospitalMetrics.builder()
                    .totalPatients(patientCount != null ? patientCount : 0L)
                    // Placeholder for other clients (Wards, Billing, etc.)
                    .totalAppointments(0L) 
                    .totalAdmissions(0L)
                    .activeAdmissions(0L)
                    .totalRevenue(0.0)
                    .updatedAt(LocalDateTime.now())
                    .build();

            // 3. Persist to Analytics Database
            metricsRepository.save(latestMetrics);
            log.info("Metrics successfully aggregated and persisted.");

        } catch (Exception e) {
            log.error("Aggregation cycle failed. Reason: {}. Will retry next cycle.", e.getMessage());
        }
    }
}