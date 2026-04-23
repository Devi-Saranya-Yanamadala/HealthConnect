package com.cts.healthconnect.slotbooking.repository;


import com.cts.healthconnect.slotbooking.entity.DoctorSlot;
import com.cts.healthconnect.slotbooking.entity.SlotStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SlotRepository extends JpaRepository<DoctorSlot, Long> {

    List<DoctorSlot> findByDoctorCodeAndSlotDate(String doctorCode, LocalDate date);

    Optional<DoctorSlot> findByIdAndStatus(Long id, SlotStatus status);
}
