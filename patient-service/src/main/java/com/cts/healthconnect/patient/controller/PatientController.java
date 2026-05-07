package com.cts.healthconnect.patient.controller;



import com.cts.healthconnect.patient.dto.*;
import com.cts.healthconnect.patient.service.PatientService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService service;

    @PostMapping
    public PatientResponseDto register(@Valid @RequestBody PatientRequestDto dto) {
        return service.registerPatient(dto);
    }

    @GetMapping("/{patientCode}")
    public PatientResponseDto get(@PathVariable String patientCode) {
        return service.getPatientByCode(patientCode);
    }

    @PutMapping("/{patientCode}/deactivate")
    public void deactivate(@PathVariable String patientCode) {
        service.deactivatePatient(patientCode);
    }

    @PutMapping("/{patientCode}/deceased")
    public PatientResponseDto markDeceased(@PathVariable String patientCode) {
        return service.markPatientDeceased(patientCode);
    }
    
    @GetMapping("/count")
    public Long getTotalPatients() {
    	return service.getTotalPatients();
    }
    
    @GetMapping("/count/by-date")
    public Long getPatientsByDate(@RequestParam String date) {
        return service.getPatientCountByDate(date);
    }
}
