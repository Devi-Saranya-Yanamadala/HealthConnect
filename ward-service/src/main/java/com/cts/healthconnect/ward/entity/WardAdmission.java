package com.cts.healthconnect.ward.entity;



import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(
    name = "ward_admissions",
    uniqueConstraints = @UniqueConstraint(columnNames = "admission_code")
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WardAdmission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "admission_code", nullable = false)
    private String admissionCode;

    private String patientCode;
    private String doctorCode;

    @Enumerated(EnumType.STRING)
    private WardType wardType;

    private String bedNumber;

    @Enumerated(EnumType.STRING)
    private AdmissionStatus status;

    @CreationTimestamp
    private LocalDateTime admittedAt;

    private LocalDateTime dischargedAt;
}

