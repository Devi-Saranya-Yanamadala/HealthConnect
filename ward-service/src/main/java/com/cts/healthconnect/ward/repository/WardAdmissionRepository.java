package com.cts.healthconnect.ward.repository;



import com.cts.healthconnect.ward.entity.WardAdmission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WardAdmissionRepository extends JpaRepository<WardAdmission, Long> {

    Optional<WardAdmission> findByAdmissionCode(String admissionCode);
}