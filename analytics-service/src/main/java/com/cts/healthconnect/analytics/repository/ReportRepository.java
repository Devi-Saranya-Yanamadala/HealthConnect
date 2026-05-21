package com.cts.healthconnect.analytics.repository;

import com.cts.healthconnect.analytics.entity.Report;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends JpaRepository<Report, String> {
	@Query("SELECT r.reportId FROM Report r WHERE r.reportId LIKE 'RPT-%' ORDER BY r.reportId DESC LIMIT 1")
	Optional<String> findLastReportId();
}
