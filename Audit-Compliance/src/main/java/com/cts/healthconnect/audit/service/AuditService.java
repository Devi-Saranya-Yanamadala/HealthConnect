package com.cts.healthconnect.audit.service;

import com.cts.healthconnect.audit.entity.*;
import com.cts.healthconnect.audit.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class AuditService {

    @Autowired private AuditRepository auditRepository;
    @Autowired private ComplianceRepository complianceRepository;

    public List<AuditLog> getAllLogs() {
        return auditRepository.findAllByOrderByTimestampDesc();
    }

    public Optional<AuditLog> getLogById(Long id) {
        return auditRepository.findById(id);
    }

    public String exportAuditLogsToCSV() {
        List<AuditLog> logs = auditRepository.findAll();
        StringBuilder csv = new StringBuilder("ID,Module,Action,User,ResourceId,Timestamp\n");
        for (AuditLog log : logs) {
            csv.append(log.getId()).append(",")
               .append(log.getModule()).append(",")
               .append(log.getAction()).append(",")
               .append(log.getPerformedBy()).append(",")
               .append(log.getResourceId()).append(",")
               .append(log.getTimestamp()).append("\n");
        }
        return csv.toString();
    }

    public List<ComplianceReport> getAllReports() {
        return complianceRepository.findAll();
    }

    public ComplianceReport generateReport(ComplianceReport report) {
        report.setStatus("COMPLETED");
        return complianceRepository.save(report);
    }
}