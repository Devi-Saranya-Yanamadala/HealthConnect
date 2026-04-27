package com.cts.healthconnect.ward.controller;


import com.cts.healthconnect.ward.dto.*;
import com.cts.healthconnect.ward.entity.WardType;
import com.cts.healthconnect.ward.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wards")
@RequiredArgsConstructor
public class WardController {

    private final WardService wardService;
    private final BedService bedService;

    @PostMapping("/admit")
    public WardAdmissionResponseDto admit(@RequestBody WardAdmissionRequestDto dto) {
        return wardService.admitPatient(dto);
    }

    @GetMapping("/{admissionCode}")
    public WardAdmissionResponseDto get(@PathVariable String admissionCode) {
        return wardService.getAdmission(admissionCode);
    }

    @PutMapping("/{admissionCode}/discharge")
    public void discharge(@PathVariable String admissionCode) {
        wardService.dischargePatient(admissionCode);
    }

    @GetMapping("/beds/available")
    public List<String> availableBeds(@RequestParam WardType wardType) {
        return bedService.getAvailableBeds(wardType);
    }
    
    @GetMapping("/admissions/count")
    public Long getTotalAdmissions() {
        return wardService.getTotalAdmissions();
    }

    @GetMapping("/admissions/active")
    public Long getActiveAdmissions() {
        return wardService.getActiveAdmissions();
    }
}