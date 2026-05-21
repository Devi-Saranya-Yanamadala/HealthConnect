package com.cts.healthconnect.doctor.repository;

import com.cts.healthconnect.doctor.entity.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {

    List<Prescription> findByPatientCode(String patientCode);

    List<Prescription> findByDoctorCode(String doctorCode);

    Optional<Prescription> findByPrescriptionCode(String prescriptionCode);

    Optional<Prescription> findByAppointmentCode(String appointmentCode);

    @Query("SELECT p.prescriptionCode FROM Prescription p WHERE p.prescriptionCode LIKE 'PRX-%' ORDER BY p.prescriptionCode DESC LIMIT 1")
    Optional<String> findLastPrescriptionCode();
}