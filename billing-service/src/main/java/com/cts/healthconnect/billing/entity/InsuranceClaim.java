package com.cts.healthconnect.billing.entity;



import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "insurance_claims",
       uniqueConstraints = @UniqueConstraint(columnNames = "claim_number"))
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class InsuranceClaim {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "claim_number", nullable = false)
    private String claimNumber;

    private String appointmentCode;
    private Long patientId;

    private String insuranceProvider;
    private String policyNumber;

    private Double claimAmount;
    private Double approvedAmount;

    @Enumerated(EnumType.STRING)
    private ClaimStatus status;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
