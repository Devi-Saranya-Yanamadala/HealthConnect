package com.cts.healthconnect.doctor.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
@Entity
@Table(name = "doctors",
       uniqueConstraints = {
           @UniqueConstraint(columnNames = "doctor_code"),
           @UniqueConstraint(columnNames = "license_number")
       })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "doctor_code", nullable = false)
    private String doctorCode;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    private String specialization;
    private String department;

    @Column(name = "license_number", nullable = false)
    private String licenseNumber;

    private String phoneNumber;
    private String email;



	@Schema(description = "Doctor working start time")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
	private LocalTime workingStartTime;
	
	@Schema(description = "Doctor working end time")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
	private LocalTime workingEndTime;


    private Boolean active;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}