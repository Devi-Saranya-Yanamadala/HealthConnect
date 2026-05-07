package com.cts.healthconnect.analytics.repository;

import com.cts.healthconnect.analytics.entity.HospitalMetrics;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HospitalMetricsRepository extends JpaRepository<HospitalMetrics, Long> {
    // ✅ Explicit override to avoid Spring Data ambiguity with paged findAll
    Page<HospitalMetrics> findAll(Pageable pageable);
}