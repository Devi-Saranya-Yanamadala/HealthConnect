package com.cts.healthconnect.slotbooking.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cts.healthconnect.slotbooking.dto.SlotCreateRequestDto;
import com.cts.healthconnect.slotbooking.dto.SlotResponseDto;
import com.cts.healthconnect.slotbooking.service.SlotService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/slots")
@RequiredArgsConstructor
public class SlotController {

    private final SlotService service;

    // ✅ Create slots (ADMIN)
    @PostMapping("/generate")
    public ResponseEntity<Void> generateSlots(@RequestBody SlotCreateRequestDto dto) {
        service.createSlots(dto);
        return ResponseEntity.ok().build();
    }

    // ✅ View slots
    @GetMapping
    public List<SlotResponseDto> getSlots(
            @RequestParam String doctorCode,
            @RequestParam LocalDate date) {
        return service.getSlots(doctorCode, date);
    }

    // ✅ Book slot
    @PutMapping("/book/{slotId}")
    public ResponseEntity<Void> bookSlot(@PathVariable Long slotId) {
        service.bookSlot(slotId);
        return ResponseEntity.noContent().build();
    }

    
    @PatchMapping("/{slotId}/release")
    public ResponseEntity<Void> releaseSlot(@PathVariable Long slotId) {
        service.releaseSlot(slotId);
        return ResponseEntity.noContent().build();
    }
}