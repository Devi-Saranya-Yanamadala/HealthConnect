package com.cts.healthconnect.appointment.repository;



import com.cts.healthconnect.appointment.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    Optional<Appointment> findByAppointmentCode(String appointmentCode);
    Optional<Appointment> findById(Long id);
boolean existsByPatientIdAndDoctorCodeAndAppointmentDate(
        Long patientId,
        String doctorCode,
        LocalDate appointmentDate
    );
Long countByAppointmentDate(LocalDate date);

}

