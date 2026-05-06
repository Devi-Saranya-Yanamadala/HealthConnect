package com.cts.healthconnect.slotbooking.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.cts.healthconnect.slotbooking.dto.SlotCreateRequestDto;
import com.cts.healthconnect.slotbooking.dto.SlotResponseDto;
import com.cts.healthconnect.slotbooking.entity.DoctorSlot;
import com.cts.healthconnect.slotbooking.entity.SlotStatus;
import com.cts.healthconnect.slotbooking.exception.SlotNotAvailableException;
import com.cts.healthconnect.slotbooking.repository.SlotRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SlotServiceImpl implements SlotService {

    private final SlotRepository repository;

    @Override
    public void createSlots(SlotCreateRequestDto dto) {

        if (dto.getDoctorCode() == null || dto.getDoctorCode().isBlank()) {
            throw new IllegalArgumentException("Doctor code is required");
        }

        if (dto.getSlotDate() == null) {
            throw new IllegalArgumentException("Slot date is required");
        }

        if (dto.getStartTime() == null || dto.getEndTime() == null) {
            throw new IllegalArgumentException("Start time and end time are required");
        }

        if (!dto.getStartTime().isBefore(dto.getEndTime())) {
            throw new IllegalArgumentException("Start time must be before end time");
        }

        LocalTime start = dto.getStartTime();

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
        }
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

        if (slot.getStatus() != SlotStatus.AVAILABLE) {
            throw new SlotNotAvailableException(slotId);
        }

        slot.setStatus(SlotStatus.BOOKED);
    }

    @Override
    @Transactional
    public void releaseSlot(Long slotId) {
        DoctorSlot slot = repository.findById(slotId)
                .orElseThrow(() -> new SlotNotAvailableException(slotId));
        slot.setStatus(SlotStatus.AVAILABLE);
    }
}
