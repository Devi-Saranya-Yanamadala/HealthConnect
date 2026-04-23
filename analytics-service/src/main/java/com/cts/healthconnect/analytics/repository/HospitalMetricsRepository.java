package com.cts.healthconnect.analytics.repository;

import com.cts.healthconnect.analytics.entity.HospitalMetrics;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HospitalMetricsRepository
        extends JpaRepository<HospitalMetrics, Long> {
}
