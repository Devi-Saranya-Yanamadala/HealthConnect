package com.cts.healthconnect.slotbooking.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cts.healthconnect.slotbooking.dto.SlotCreateRequestDto;
import com.cts.healthconnect.slotbooking.dto.SlotResponseDto;
import com.cts.healthconnect.slotbooking.service.SlotService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/slots")
@RequiredArgsConstructor
public class SlotController {

    private final SlotService service;

    @PostMapping("/generate")
    public void generateSlots(@RequestBody SlotCreateRequestDto dto) {
        service.createSlots(dto);
    }

    @GetMapping
    public List<SlotResponseDto> getSlots(
            @RequestParam String doctorCode,
            @RequestParam LocalDate date) {
        return service.getSlots(doctorCode, date);
    }

    @PutMapping("/book/{slotId}")
    public void bookSlot(@PathVariable Long slotId) {
        service.bookSlot(slotId);
    }

    @PutMapping("/release/{slotId}")
    public void releaseSlot(@PathVariable Long slotId) {
        service.releaseSlot(slotId);
    }
}
