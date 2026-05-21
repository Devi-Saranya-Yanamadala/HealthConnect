package com.cts.healthconnect.doctor.service;

import com.cts.healthconnect.doctor.dto.PrescriptionRequestDto;
import com.cts.healthconnect.doctor.dto.PrescriptionResponseDto;

import java.util.List;

public interface PrescriptionService {

    PrescriptionResponseDto createPrescription(PrescriptionRequestDto dto);

    PrescriptionResponseDto getByPrescriptionCode(String code);

    PrescriptionResponseDto getByAppointmentCode(String appointmentCode);

    List<PrescriptionResponseDto> getByPatientCode(String patientCode);

    List<PrescriptionResponseDto> getByDoctorCode(String doctorCode);
}