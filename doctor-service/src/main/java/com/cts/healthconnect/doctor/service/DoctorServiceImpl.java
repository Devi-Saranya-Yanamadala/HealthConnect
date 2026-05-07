package com.cts.healthconnect.doctor.service;

import com.cts.healthconnect.doctor.client.AuditClient;
import com.cts.healthconnect.doctor.dto.DoctorRequestDto;
import com.cts.healthconnect.doctor.dto.DoctorResponseDto;
import com.cts.healthconnect.doctor.entity.Doctor;
import com.cts.healthconnect.doctor.exception.DoctorNotFoundException;
import com.cts.healthconnect.doctor.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository repository;
    private final AuditClient      auditClient;

    @Override
    public DoctorResponseDto createDoctor(DoctorRequestDto dto) {
        Doctor doctor = Doctor.builder()
                .doctorCode(dto.getDoctorCode())
                .fullName(dto.getFullName())
                .specialization(dto.getSpecialization())
                .department(dto.getDepartment())
                .licenseNumber(dto.getLicenseNumber())
                .phoneNumber(dto.getPhoneNumber())
                .email(dto.getEmail())
                .workingStartTime(dto.getWorkingStartTime())
                .workingEndTime(dto.getWorkingEndTime())
                .active(true)
                .build();

        repository.save(doctor);

        // ✅ AUDIT LOG
        audit("CREATE_DOCTOR", doctor.getDoctorCode(),
              "Doctor created: " + dto.getFullName()
              + " | Dept: " + dto.getDepartment());

        return mapToResponse(doctor);
    }

    @Override
    public DoctorResponseDto getDoctorByCode(String code) {
        Doctor doctor = repository.findByDoctorCode(code)
                .orElseThrow(() -> new DoctorNotFoundException(code));
        return mapToResponse(doctor);
    }

    @Override
    public List<DoctorResponseDto> getActiveDoctors() {
        return repository.findByActiveTrue()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public void deactivateDoctor(String code) {
        Doctor doctor = repository.findByDoctorCode(code)
                .orElseThrow(() -> new DoctorNotFoundException(code));
        doctor.setActive(false);

        // ✅ AUDIT LOG
        audit("DEACTIVATE_DOCTOR", code,
              "Doctor deactivated: " + doctor.getFullName());
    }

    // ✅ Audit helper
    private void audit(String action, String resourceId, String details) {
        try {
            auditClient.log(Map.of(
                "module",      "DOCTOR",
                "action",      action,
                "performedBy", "system",
                "resourceId",  resourceId != null ? resourceId : "",
                "details",     details != null ? details : ""
            ));
        } catch (Exception e) {
            System.err.println(">>> AUDIT FAILED [DOCTOR]: " + e.getMessage());
        }
    }

    private DoctorResponseDto mapToResponse(Doctor doctor) {
        return DoctorResponseDto.builder()
                .id(doctor.getId())
                .doctorCode(doctor.getDoctorCode())
                .fullName(doctor.getFullName())
                .specialization(doctor.getSpecialization())
                .department(doctor.getDepartment())
                .active(doctor.getActive())
                .build();
    }
}