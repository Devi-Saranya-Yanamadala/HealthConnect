package com.cts.healthconnect.appointment.entity;



import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(

name = "appointments",
  uniqueConstraints = {
      @UniqueConstraint(
          columnNames = {"patient_id", "doctor_code", "appointment_date","appointment_code"}
      )
  }
)

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "appointment_code", nullable = false)
    private String appointmentCode;

    @Column(name = "patient_id", nullable = false)
    private Long patientId;

    @Column(name = "doctor_code", nullable = false)
    private String doctorCode;

    @Column(name = "slot_id", nullable = false)
    private Long slotId;

    private LocalDate appointmentDate;

    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;

    @CreationTimestamp
    private LocalDateTime createdAt;
}