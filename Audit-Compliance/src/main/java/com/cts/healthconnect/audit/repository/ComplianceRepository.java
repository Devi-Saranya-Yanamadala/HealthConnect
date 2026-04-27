package com.cts.healthconnect.audit.repository;

import com.cts.healthconnect.audit.entity.ComplianceReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComplianceRepository extends JpaRepository<ComplianceReport, Long> {
}