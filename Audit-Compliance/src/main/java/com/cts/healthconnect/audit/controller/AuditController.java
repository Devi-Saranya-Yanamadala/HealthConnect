package com.cts.healthconnect.audit.controller;

import com.cts.healthconnect.audit.entity.*;
import com.cts.healthconnect.audit.service.AuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class AuditController {

    @Autowired private AuditService auditService;

    // 1. Search audit logs
    @GetMapping("/audit/logs")
    public List<AuditLog> searchLogs() {
        return auditService.getAllLogs();
    }

    // 2. Get audit entry
    @GetMapping("/audit/logs/{auditId}")
    public ResponseEntity<AuditLog> getEntry(@PathVariable Long auditId) {
        return auditService.getLogById(auditId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 3. Export audit logs
    @PostMapping("/audit/logs/export")
    public ResponseEntity<byte[]> export() {
        byte[] data = auditService.exportAuditLogsToCSV().getBytes();
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=audit_report.csv");
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(data);
    }

    // 4. List compliance reports
    @GetMapping("/compliance/reports")
    public List<ComplianceReport> listReports() {
        return auditService.getAllReports();
    }

    // 5. Generate compliance report
    @PostMapping("/compliance/reports/generate")
    public ResponseEntity<ComplianceReport> generate(@RequestBody ComplianceReport report) {
        return ResponseEntity.ok(auditService.generateReport(report));
    }
}