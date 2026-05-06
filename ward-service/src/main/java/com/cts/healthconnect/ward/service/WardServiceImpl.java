package com.cts.healthconnect.ward.service;


import com.cts.healthconnect.ward.client.PatientClient;
import com.cts.healthconnect.ward.dto.*;
import com.cts.healthconnect.ward.entity.*;
import com.cts.healthconnect.ward.exception.*;
import com.cts.healthconnect.ward.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class WardServiceImpl implements WardService {

    private final WardAdmissionRepository admissionRepository;
    private final BedRepository bedRepository;
    private final PatientClient patientClient;

    @Override
    public WardAdmissionResponseDto admitPatient(WardAdmissionRequestDto dto) {
    	
    	PatientResponseDto patient = 
    			patientClient.getPatient(dto.getPatientCode());
    	
    	// if patient doesn't exist
		if(!Boolean.TRUE.equals(patient.getActive())) {
		    throw new IllegalStateException("Patient is inactive");
		}


        Bed bed = bedRepository.findByBedNumber(dto.getBedNumber())
                .orElseThrow(() -> new BedNotAvailableException(dto.getBedNumber()));

        // if bed is not vacant
        if (Boolean.TRUE.equals(bed.getOccupied())) {
            throw new BedNotAvailableException(dto.getBedNumber());
        }

        bed.setOccupied(true);

        WardAdmission admission = WardAdmission.builder()
                .admissionCode(UUID.randomUUID().toString())
                .patientCode(dto.getPatientCode())
                .doctorCode(dto.getDoctorCode())
                .wardType(dto.getWardType())
                .bedNumber(dto.getBedNumber())
                .status(AdmissionStatus.ADMITTED)
                .build();

        admissionRepository.save(admission);
        return map(admission);
    }

    @Override
    public WardAdmissionResponseDto getAdmission(String admissionCode) {

        WardAdmission admission = admissionRepository.findByAdmissionCode(admissionCode)
                .orElseThrow(() -> new AdmissionNotFoundException(admissionCode));

        return map(admission);
    }

    @Override
    public void dischargePatient(String admissionCode) {

        WardAdmission admission = admissionRepository.findByAdmissionCode(admissionCode)
                .orElseThrow(() -> new AdmissionNotFoundException(admissionCode));

        admission.setStatus(AdmissionStatus.DISCHARGED);
        admission.setDischargedAt(LocalDateTime.now());

        Bed bed = bedRepository.findByBedNumber(admission.getBedNumber())
                .orElseThrow();

        bed.setOccupied(false);
    }
    

    @Override
    public Long getTotalAdmissions() {
        return admissionRepository.count();
    }

    @Override
    public Long getActiveAdmissions() {
        return admissionRepository.countByStatus(AdmissionStatus.ADMITTED);
    }


    private WardAdmissionResponseDto map(WardAdmission a) {
        return WardAdmissionResponseDto.builder()
                .admissionCode(a.getAdmissionCode())
                .patientCode(a.getPatientCode())
                .doctorCode(a.getDoctorCode())
                .wardType(a.getWardType().name())
                .bedNumber(a.getBedNumber())
                .status(a.getStatus().name())
                .build();
    }
}
