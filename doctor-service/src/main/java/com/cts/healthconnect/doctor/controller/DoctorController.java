package com.cts.healthconnect.doctor.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cts.healthconnect.doctor.dto.DoctorRequestDto;
import com.cts.healthconnect.doctor.dto.DoctorResponseDto;
import com.cts.healthconnect.doctor.service.DoctorService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor

public class DoctorController {

    private final DoctorService service;

    @PostMapping
    public DoctorResponseDto create(@RequestBody DoctorRequestDto dto) {
        return service.createDoctor(dto);
    }

    @GetMapping("/{code}")
    public DoctorResponseDto getByCode(@PathVariable String code) {
        return service.getDoctorByCode(code);
    }

    @GetMapping
    public List<DoctorResponseDto> getActiveDoctors() {
        return service.getActiveDoctors();
    }

    @PutMapping("/{code}/deactivate")
    public void deactivate(@PathVariable String code) {
        service.deactivateDoctor(code);
    }
}