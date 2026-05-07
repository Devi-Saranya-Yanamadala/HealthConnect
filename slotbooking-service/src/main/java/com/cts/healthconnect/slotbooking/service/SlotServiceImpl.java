package com.cts.healthconnect.slotbooking.service;

import com.cts.healthconnect.slotbooking.client.AuditClient;
import com.cts.healthconnect.slotbooking.dto.SlotCreateRequestDto;
import com.cts.healthconnect.slotbooking.dto.SlotResponseDto;
import com.cts.healthconnect.slotbooking.entity.DoctorSlot;
import com.cts.healthconnect.slotbooking.entity.SlotStatus;
import com.cts.healthconnect.slotbooking.exception.SlotNotAvailableException;
import com.cts.healthconnect.slotbooking.repository.SlotRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SlotServiceImpl implements SlotService {

    private final SlotRepository repository;
    private final AuditClient    auditClient;

    @Override
    public void createSlots(SlotCreateRequestDto dto) {

        if (dto.getDoctorCode() == null || dto.getDoctorCode().isBlank())
            throw new IllegalArgumentException("Doctor code is required");

        if (dto.getSlotDate() == null)
            throw new IllegalArgumentException("Slot date is required");

        if (dto.getStartTime() == null || dto.getEndTime() == null)
            throw new IllegalArgumentException("Start time and end time are required");

        if (!dto.getStartTime().isBefore(dto.getEndTime()))
            throw new IllegalArgumentException("Start time must be before end time");

        LocalTime start = dto.getStartTime();
        int slotCount = 0;

        while (start.plusMinutes(dto.getSlotDuration()).isBefore(dto.getEndTime())
                || start.plusMinutes(dto.getSlotDuration()).equals(dto.getEndTime())) {

            LocalTime end = start.plusMinutes(dto.getSlotDuration());

            DoctorSlot slot = DoctorSlot.builder()
                    .doctorCode(dto.getDoctorCode())
                    .slotDate(dto.getSlotDate())
                    .startTime(start)
                    .endTime(end)
                    .status(SlotStatus.AVAILABLE)
                    .build();

            repository.save(slot);
            start = end;
            slotCount++;
        }

        // ✅ AUDIT LOG — one log for the whole generation batch
        audit("GENERATE_SLOTS", dto.getDoctorCode(),
              "Doctor: " + dto.getDoctorCode()
              + " | Date: " + dto.getSlotDate()
              + " | Slots generated: " + slotCount
              + " | Duration: " + dto.getSlotDuration() + " mins");
    }

    @Override
    public List<SlotResponseDto> getSlots(String doctorCode, LocalDate date) {
        return repository.findByDoctorCodeAndSlotDate(doctorCode, date)
                .stream()
                .map(slot -> new SlotResponseDto(
                        slot.getId(),
                        slot.getStartTime(),
                        slot.getEndTime(),
                        slot.getStatus()
                ))
                .toList();
    }

    @Override
    @Transactional
    public void bookSlot(Long slotId) {
        DoctorSlot slot = repository.findById(slotId)
            .orElseThrow(() -> new SlotNotAvailableException(slotId));

        if (slot.getStatus() != SlotStatus.AVAILABLE)
            throw new SlotNotAvailableException(slotId);

        slot.setStatus(SlotStatus.BOOKED);

        // ✅ AUDIT LOG
        audit("BOOK_SLOT", String.valueOf(slotId),
              "Slot booked | Doctor: " + slot.getDoctorCode()
              + " | Date: " + slot.getSlotDate()
              + " | Time: " + slot.getStartTime());
    }

    @Override
    @Transactional
    public void releaseSlot(Long slotId) {
        DoctorSlot slot = repository.findById(slotId)
                .orElseThrow(() -> new SlotNotAvailableException(slotId));
        slot.setStatus(SlotStatus.AVAILABLE);

        // ✅ AUDIT LOG
        audit("RELEASE_SLOT", String.valueOf(slotId),
              "Slot released | Doctor: " + slot.getDoctorCode()
              + " | Date: " + slot.getSlotDate());
    }

    // ✅ Audit helper — never throws
    private void audit(String action, String resourceId, String details) {
        try {
            auditClient.log(Map.of(
                "module",      "SLOT",
                "action",      action,
                "performedBy", "system",
                "resourceId",  resourceId != null ? resourceId : "",
                "details",     details != null ? details : ""
            ));
        } catch (Exception e) {
            System.err.println(">>> AUDIT FAILED [SLOT]: " + e.getMessage());
        }
    }
}