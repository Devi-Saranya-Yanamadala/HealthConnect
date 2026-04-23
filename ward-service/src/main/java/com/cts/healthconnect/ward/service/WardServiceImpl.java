package com.cts.healthconnect.ward.service;


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

    @Override
    public WardAdmissionResponseDto admitPatient(WardAdmissionRequestDto dto) {

        Bed bed = bedRepository.findByBedNumber(dto.getBedNumber())
                .orElseThrow(() -> new BedNotAvailableException(dto.getBedNumber()));

        if (Boolean.TRUE.equals(bed.getOccupied())) {
            throw new BedNotAvailableException(dto.getBedNumber());
        }

        bed.setOccupied(true);

        WardAdmission admission = WardAdmission.builder()
                .admissionCode(UUID.randomUUID().toString())
                .patientId(dto.getPatientId())
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

    private WardAdmissionResponseDto map(WardAdmission a) {
        return WardAdmissionResponseDto.builder()
                .admissionCode(a.getAdmissionCode())
                .patientId(a.getPatientId())
                .doctorCode(a.getDoctorCode())
                .wardType(a.getWardType().name())
                .bedNumber(a.getBedNumber())
                .status(a.getStatus().name())
                .build();
    }
}
