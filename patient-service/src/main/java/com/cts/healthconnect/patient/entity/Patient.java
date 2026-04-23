package com.cts.healthconnect.patient.entity;



import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "patients",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = "patient_code"),
        @UniqueConstraint(columnNames = "phone"),
        @UniqueConstraint(columnNames = "national_id")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "patient_code", nullable = false)
    private String patientCode;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private LocalDate dob;

    @Column(nullable = false)
    private String phone;

    private String email;

    @Enumerated(EnumType.STRING)
    private BloodGroup bloodGroup;

    private String address;

    /* Emergency contact */
    private String emergencyContactName;
    private String emergencyContactPhone;

    /* Government / National ID */
    @Column(name = "national_id")
    private String nationalId;

    @Enumerated(EnumType.STRING)
    private PatientStatus status;

    /* Soft delete */
    private Boolean active;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
