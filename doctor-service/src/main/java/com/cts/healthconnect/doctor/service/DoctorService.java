package com.cts.healthconnect.doctor.service;

import java.util.List;

import com.cts.healthconnect.doctor.dto.DoctorRequestDto;
import com.cts.healthconnect.doctor.dto.DoctorResponseDto;

public interface DoctorService {

    DoctorResponseDto createDoctor(DoctorRequestDto dto);

    DoctorResponseDto getDoctorByCode(String doctorCode);

    List<DoctorResponseDto> getActiveDoctors();

    void deactivateDoctor(String doctorCode);
}
