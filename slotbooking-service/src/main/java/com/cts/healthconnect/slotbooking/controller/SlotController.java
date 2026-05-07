package com.cts.healthconnect.slotbooking.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.cts.healthconnect.slotbooking.dto.SlotCreateRequestDto;
import com.cts.healthconnect.slotbooking.dto.SlotResponseDto;
import com.cts.healthconnect.slotbooking.service.SlotService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/slots")
@RequiredArgsConstructor
@Validated // <--- Add this at the class level to validate single parameters
public class SlotController {

    private final SlotService service;

    // Create slots (ADMIN)
    @PostMapping("/generate")
    public ResponseEntity<Void> generateSlots(@Valid @RequestBody SlotCreateRequestDto dto) {
        service.createSlots(dto);
        return ResponseEntity.ok().build();
    }

    // View slots
    @GetMapping
    public List<SlotResponseDto> getSlots(
            @RequestParam @NotBlank String doctorCode,
            @RequestParam @NotNull @FutureOrPresent LocalDate date) {
        return service.getSlots(doctorCode, date);
    }

    // Book slot
    @PutMapping("/book/{slotId}")
    public ResponseEntity<Void> bookSlot(@PathVariable @Min(1) Long slotId) {
        service.bookSlot(slotId);
        return ResponseEntity.noContent().build();
    }

    // release slot
    @PatchMapping("/{slotId}/release")
    public ResponseEntity<Void> releaseSlot(@PathVariable Long slotId) {
        service.releaseSlot(slotId);
        return ResponseEntity.noContent().build();
    }
}