package com.cts.healthconnect.audit.repository;

import com.cts.healthconnect.audit.entity.ComplianceReport;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ComplianceRepository extends JpaRepository<ComplianceReport, Long> {
	@Query("SELECT c.reportCode FROM ComplianceReport c WHERE c.reportCode LIKE 'CMP-%' ORDER BY c.reportCode DESC LIMIT 1")
	Optional<String> findLastReportCode();
}