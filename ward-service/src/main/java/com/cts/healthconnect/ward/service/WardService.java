package com.cts.healthconnect.ward.service;




import com.cts.healthconnect.ward.dto.*;

public interface WardService {

    WardAdmissionResponseDto admitPatient(WardAdmissionRequestDto dto);

    WardAdmissionResponseDto getAdmission(String admissionCode);

    void dischargePatient(String admissionCode);
}
