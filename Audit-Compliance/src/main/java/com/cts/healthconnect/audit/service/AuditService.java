package com.cts.healthconnect.audit.service;

import com.cts.healthconnect.audit.dto.ComplianceReportRequestDto;
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
        StringBuilder csv = new StringBuilder(
            "ID,Module,Action,User,ResourceId,Timestamp\n"
        );
        for (AuditLog log : logs) {
            csv.append(nullSafe(log.getId())).append(",")
               .append(nullSafe(log.getModule())).append(",")
               .append(nullSafe(log.getAction())).append(",")
               .append(nullSafe(log.getPerformedBy())).append(",")
               .append(nullSafe(log.getResourceId())).append(",")
               .append(nullSafe(log.getTimestamp())).append("\n");
        }
        return csv.toString();
    }

    public List<ComplianceReport> getAllReports() {
        return complianceRepository.findAll();
    }

    private String generateComplianceReportCode() {
        return complianceRepository.findLastReportCode()
            .map(last -> {
                // last is like "CMP-0005", extract number after "CMP-"
                int num = Integer.parseInt(last.substring(4));
                return String.format("CMP-%04d", num + 1);
            })
            .orElse("CMP-0001");
    }

    public ComplianceReport generateReport(ComplianceReportRequestDto request) {
        ComplianceReport report = new ComplianceReport();
        report.setReportCode(generateComplianceReportCode());   // ← ADD THIS LINE
        report.setReportName(request.getReportName());
        report.setGeneratedBy(
            request.getGeneratedBy() != null ? request.getGeneratedBy() : "admin"
        );
        report.setStatus("COMPLETED");
        return complianceRepository.save(report);
    }
    
    public AuditLog createLog(AuditLog log) {
        return auditRepository.save(log);
    }
    
    private String nullSafe(Object val) {
        return val == null ? "" : val.toString();
    }
}