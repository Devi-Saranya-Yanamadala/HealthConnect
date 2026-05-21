package com.cts.healthconnect.appointment.repository;



import com.cts.healthconnect.appointment.entity.Appointment;
import com.cts.healthconnect.appointment.entity.AppointmentStatus;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    Optional<Appointment> findByAppointmentCode(String appointmentCode);
    
    Optional<Appointment> findById(Long id);
    
	/*boolean existsByPatientIdAndDoctorCodeAndAppointmentDate(
	        Long patientId,
	        String doctorCode,
	        LocalDate appointmentDate
	    );*/
    boolean existsByPatientIdAndDoctorCodeAndAppointmentDateAndStatus(
    	    Long patientId, String doctorCode, 
    	    LocalDate appointmentDate, AppointmentStatus status);
    
	Long countByAppointmentDate(LocalDate date);
	
	List<Appointment> findByPatientId(Long patientId);
	
	List<Appointment> findByDoctorCodeOrderByAppointmentDateDesc(String doctorCode);
	

}

