package com.cts.healthconnect.audit.controller;

import com.cts.healthconnect.audit.dto.ComplianceReportRequestDto;
import com.cts.healthconnect.audit.entity.*;
import com.cts.healthconnect.audit.service.AuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/api")
public class AuditController {

    @Autowired
    private AuditService auditService;

    // 1. Get all audit logs
    @GetMapping("/audit/logs")
    public ResponseEntity<List<AuditLog>> searchLogs() {
        return ResponseEntity.ok(auditService.getAllLogs());
    }

    // 2. Get audit log by ID
    @GetMapping("/audit/logs/{auditId}")
    public ResponseEntity<AuditLog> getEntry(@PathVariable Long auditId) {
        return auditService.getLogById(auditId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 3. Export audit logs as CSV
    @PostMapping("/audit/logs/export")
    public ResponseEntity<byte[]> export() {
        byte[] data = auditService.exportAuditLogsToCSV()
                                  .getBytes(StandardCharsets.UTF_8);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(
            MediaType.parseMediaType("text/csv; charset=UTF-8")
        );
        headers.setContentDisposition(
            ContentDisposition.attachment()
                .filename("audit_report.csv")
                .build()
        );
        return ResponseEntity.ok().headers(headers).body(data);
    }

    // 4. List all compliance reports
    @GetMapping("/compliance/reports")
    public ResponseEntity<List<ComplianceReport>> listReports() {
        return ResponseEntity.ok(auditService.getAllReports());
    }

    // 5. Generate compliance report
    @PostMapping("/compliance/reports/generate")
    public ResponseEntity<ComplianceReport> generate(
            @RequestBody ComplianceReportRequestDto request) {
        return ResponseEntity.ok(auditService.generateReport(request));
    }

    // ✅ ADDED: other services POST audit events here
    @PostMapping("/audit/logs")
    public ResponseEntity<AuditLog> createLog(@RequestBody AuditLog log) {
        return ResponseEntity.ok(auditService.createLog(log));
    }
}