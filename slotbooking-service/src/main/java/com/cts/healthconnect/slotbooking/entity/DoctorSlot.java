package com.cts.healthconnect.slotbooking.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(
    name = "doctor_slots",
    uniqueConstraints = {
        @UniqueConstraint(
            columnNames = {"doctor_code", "slot_date", "start_time", "end_time"}
        )
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "doctor_code", nullable = false)
    private String doctorCode;

    @Column(name = "slot_date", nullable = false)
    private LocalDate slotDate;
    
    @Column(nullable = false)
    private LocalTime startTime;
    
    @Column(nullable = false)
    private LocalTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SlotStatus status=SlotStatus.AVAILABLE; // Default value

    @CreationTimestamp
    private LocalDateTime createdAt;
}