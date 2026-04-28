package com.cts.healthconnect.analytics.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "hospital_metrics")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class HospitalMetrics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long totalPatients;
    private Long totalAppointments;
    private Long totalAdmissions;
    private Long activeAdmissions;
    private Double totalRevenue;
    private LocalDateTime updatedAt;
}