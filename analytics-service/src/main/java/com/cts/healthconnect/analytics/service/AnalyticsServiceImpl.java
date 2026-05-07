package com.cts.healthconnect.analytics.service;

import com.cts.healthconnect.analytics.client.*;
import com.cts.healthconnect.analytics.dto.*;
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
    private final PatientClient             patientClient;
    private final AppointmentClient         appointmentClient;
    private final BillingClient             billingClient;
    private final WardClient                wardClient;

    /* ─── reads from DB snapshot ─── */
    private HospitalMetrics getLatest() {
        return repository.findAll(
            PageRequest.of(0, 1, Sort.by("updatedAt").descending())
        ).getContent().stream().findFirst()
         .orElseThrow(() -> new RuntimeException("No data found"));
    }

    @Override
    public AnalyticsResponseDto getDashboardMetrics() {
        HospitalMetrics m = getLatest();
        return AnalyticsResponseDto.builder()
                .totalPatients(m.getTotalPatients())
                .totalAppointments(m.getTotalAppointments())
                .totalAdmissions(m.getTotalAdmissions())
                .activeAdmissions(m.getActiveAdmissions())
                .totalRevenue(m.getTotalRevenue())
                .build();
    }

    @Override
    public KpiResponseDto getKpis() {
        HospitalMetrics m = getLatest();
        return KpiResponseDto.builder()
                .totalPatients(m.getTotalPatients())
                .totalAppointments(m.getTotalAppointments())
                .totalAdmissions(m.getTotalAdmissions())
                .activeAdmissions(m.getActiveAdmissions())
                .totalRevenue(m.getTotalRevenue())
                .build();
    }

    /* ─── REAL TIME: fetches live date-wise data from all microservices ─── */
    @Override
    public AnalyticsResponseDto getRealTimeMetrics(String date) {

        long   totalPatients     = fetchSafe(() -> patientClient.getPatientsByDate(date),         0L);
        long   totalAppointments = fetchSafe(() -> appointmentClient.getAppointmentsByDate(date), 0L);
        long   totalAdmissions   = fetchSafe(() -> wardClient.getAdmissionsByDate(date),          0L);
        long   activeAdmissions  = fetchSafe(() -> wardClient.getActiveAdmissionsByDate(date),    0L);
        double totalRevenue      = fetchSafe(() -> billingClient.getRevenueByDate(date),          0.0);

        System.out.println(">>> DAILY [" + date + "]:"
            + " patients="     + totalPatients
            + " appointments=" + totalAppointments
            + " admissions="   + totalAdmissions
            + " active="       + activeAdmissions
            + " revenue="      + totalRevenue);

        return AnalyticsResponseDto.builder()
                .totalPatients(totalPatients)
                .totalAppointments(totalAppointments)
                .totalAdmissions(totalAdmissions)
                .activeAdmissions(activeAdmissions)
                .totalRevenue(totalRevenue)
                .build();
    }

    /* ─── safe Feign wrapper — one failure won't block others ─── */
    @FunctionalInterface
    interface FeignCall<T> {
        T call() throws Exception;
    }

    private <T> T fetchSafe(FeignCall<T> call, T fallback) {
        try {
            T result = call.call();
            System.out.println(">>> FETCH OK: " + result);
            return result;
        } catch (Exception e) {
            System.err.println(">>> FETCH FAILED: " + e.getMessage());
            return fallback;
        }
    }
}