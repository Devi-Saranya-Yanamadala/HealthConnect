package com.cts.healthconnect.analytics.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reports")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String reportId;
    
    private String reportName;
    private String reportType;
    private String status; 
    private String generatedBy;
    private LocalDateTime createdAt;


    @OneToOne(mappedBy = "report", cascade = CascadeType.ALL)
    private ReportContent content;
}