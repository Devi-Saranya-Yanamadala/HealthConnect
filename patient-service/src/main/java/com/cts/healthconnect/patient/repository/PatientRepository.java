package com.cts.healthconnect.patient.repository;



import com.cts.healthconnect.patient.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import com.cts.healthconnect.patient.entity.Patient;

import java.time.LocalDateTime;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    Optional<Patient> findByPatientCode(String patientCode);

    Optional<Patient> findByPhone(String phone);
    Long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}
