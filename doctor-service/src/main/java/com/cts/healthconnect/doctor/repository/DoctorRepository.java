package com.cts.healthconnect.doctor.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cts.healthconnect.doctor.entity.Doctor;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    Optional<Doctor> findByDoctorCode(String doctorCode);

    List<Doctor> findByActiveTrue();
    @Query("SELECT d.doctorCode FROM Doctor d WHERE d.doctorCode LIKE 'DOC%' ORDER BY d.doctorCode DESC LIMIT 1")
    Optional<String> findLastDoctorCode();
    Optional<Doctor> findByEmail(String email);
}
