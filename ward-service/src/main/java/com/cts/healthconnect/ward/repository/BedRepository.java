package com.cts.healthconnect.ward.repository;



import com.cts.healthconnect.ward.entity.Bed;
import com.cts.healthconnect.ward.entity.WardType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BedRepository extends JpaRepository<Bed, Long> {

    List<Bed> findByWardTypeAndOccupiedFalse(WardType wardType);

    Optional<Bed> findByBedNumber(String bedNumber);
}

