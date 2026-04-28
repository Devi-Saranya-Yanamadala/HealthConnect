package com.cts.healthconnect.analytics.service;

import com.cts.healthconnect.analytics.client.PatientClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnalyticsAggregationServiceImpl {

    private final PatientClient patientClient;

    @Scheduled(fixedRate = 60000) // Every 1 minute for testing
    public void aggregateMetrics() {
        log.info("Fetching metrics from external services...");
        try {
            Long patientCount = patientClient.getTotalPatients();
            log.info("Aggregated Total Patients: {}", patientCount);
            // Logic to save this to analytics_db goes here
        } catch (Exception e) {
            log.warn("Could not reach patient-service. Aggregation skipped. Error: {}", e.getMessage());
        }
    }
}