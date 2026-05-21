package com.cts.healthconnect.audit.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "compliance_reports")
@Data @NoArgsConstructor @AllArgsConstructor
public class ComplianceReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "report_code", unique = true)
    private String reportCode;          // ← ADD THIS FIELD (e.g. CMP-0001)

    private String reportName;
    private String generatedBy;
    private String status;
    private LocalDateTime generatedAt;

    @PrePersist
    public void onPrePersist() { this.generatedAt = LocalDateTime.now(); }
}