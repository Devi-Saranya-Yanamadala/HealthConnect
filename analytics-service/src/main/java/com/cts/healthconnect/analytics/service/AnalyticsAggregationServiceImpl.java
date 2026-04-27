package com.cts.healthconnect.analytics.service;


import java.time.LocalDateTime;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.cts.healthconnect.analytics.client.AppointmentClient;
import com.cts.healthconnect.analytics.client.BillingClient;
import com.cts.healthconnect.analytics.client.PatientClient;
import com.cts.healthconnect.analytics.client.WardClient;
import com.cts.healthconnect.analytics.entity.HospitalMetrics;
import com.cts.healthconnect.analytics.repository.HospitalMetricsRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;



@Service
@RequiredArgsConstructor
@Slf4j
public class AnalyticsAggregationServiceImpl {

    private final PatientClient patientClient;
    private final AppointmentClient appointmentClient;
    private final BillingClient billingClient;
    private final WardClient wardClient;
    private final HospitalMetricsRepository repository;

    @Scheduled(fixedRate = 300000) // every 5 minutes
    public void aggregateMetrics() {

        log.info("Starting KPI aggregation job");

        Long totalPatients = patientClient.getTotalPatients();
        Long totalAppointments = appointmentClient.getTotalAppointments();
        Long totalAdmissions = wardClient.getTotalAdmissions();
        Long activeAdmissions = wardClient.getActiveAdmissions();
        Double totalRevenue = billingClient.getTotalRevenue();

        HospitalMetrics metrics = HospitalMetrics.builder()
                .totalPatients(totalPatients)
                .totalAppointments(totalAppointments)
                .totalAdmissions(totalAdmissions)
                .activeAdmissions(activeAdmissions)
                .totalRevenue(totalRevenue)
                .updatedAt(LocalDateTime.now())
                .build();

        repository.save(metrics);

        log.info("KPI aggregation completed successfully");
    }
}
