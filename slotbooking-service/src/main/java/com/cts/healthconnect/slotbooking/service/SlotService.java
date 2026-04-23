package com.cts.healthconnect.slotbooking.service;



import com.cts.healthconnect.slotbooking.dto.SlotCreateRequestDto;
import com.cts.healthconnect.slotbooking.dto.SlotResponseDto;

import java.time.LocalDate;
import java.util.List;

public interface SlotService {

    void createSlots(SlotCreateRequestDto dto);

    List<SlotResponseDto> getSlots(String doctorCode, LocalDate date);

    void bookSlot(Long slotId);

    void releaseSlot(Long slotId);
}
