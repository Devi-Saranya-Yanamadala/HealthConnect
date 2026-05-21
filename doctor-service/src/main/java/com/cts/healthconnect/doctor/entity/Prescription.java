package com.cts.healthconnect.doctor.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "prescriptions",
       uniqueConstraints = @UniqueConstraint(columnNames = "prescription_code"))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "prescription_code", nullable = false)
    private String prescriptionCode;   // auto-generated: PRX-001

    @Column(name = "appointment_code", nullable = false)
    private String appointmentCode;

    @Column(name = "patient_code", nullable = false)
    private String patientCode;

    @Column(name = "doctor_code", nullable = false)
    private String doctorCode;

    // Chief complaint / diagnosis
    @Column(columnDefinition = "TEXT")
    private String diagnosis;

    // Medicines as JSON string: "[{name,dosage,frequency,duration,instructions}]"
    @Column(columnDefinition = "TEXT")
    private String medicinesJson;

    // Doctor's free-text notes
    @Column(columnDefinition = "TEXT")
    private String notes;

    // Follow-up date as a string e.g. "2025-07-15"
    private String followUpDate;

    @CreationTimestamp
    private LocalDateTime createdAt;
}