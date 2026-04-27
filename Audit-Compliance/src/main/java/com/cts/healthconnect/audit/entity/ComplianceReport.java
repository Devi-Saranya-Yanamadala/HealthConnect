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
    private String reportName;
    private String generatedBy;
    private String status; 
    private LocalDateTime generatedAt;

    @PrePersist
    public void onPrePersist() { this.generatedAt = LocalDateTime.now(); }
}