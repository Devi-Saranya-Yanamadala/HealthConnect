package com.cts.healthconnect.analytics.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "report_details")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ReportContent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "report_id")
    private Report report;

    @Column(columnDefinition = "LONGTEXT")
    private String hospitalDataJson; // Stores the snapshot from Feign clients
}