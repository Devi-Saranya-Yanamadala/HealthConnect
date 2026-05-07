package com.cts.healthconnect.ward.repository;



import com.cts.healthconnect.ward.entity.AdmissionStatus;
import com.cts.healthconnect.ward.entity.WardAdmission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface WardAdmissionRepository extends JpaRepository<WardAdmission, Long> {

    Optional<WardAdmission> findByAdmissionCode(String admissionCode);
    
    Long countByStatus(AdmissionStatus status);
    Long countByAdmittedAtBetween(LocalDateTime start, LocalDateTime end);
    Long countByStatusAndAdmittedAtBetween(AdmissionStatus status, LocalDateTime start, LocalDateTime end);
}