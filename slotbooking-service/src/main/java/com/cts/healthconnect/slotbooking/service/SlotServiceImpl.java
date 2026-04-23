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

        LocalTime start = dto.getStartTime();

        while (start.isBefore(dto.getEndTime())) {
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
        DoctorSlot slot = repository.findByIdAndStatus(slotId, SlotStatus.AVAILABLE)
                .orElseThrow(() -> new SlotNotAvailableException(slotId));
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

