package com.cts.healthconnect.patient.service;



import com.cts.healthconnect.patient.dto.*;

public interface PatientService {

    PatientResponseDto registerPatient(PatientRequestDto dto);

    PatientResponseDto getPatientByCode(String patientCode);

    void deactivatePatient(String patientCode);

    PatientResponseDto markPatientDeceased(String patientCode);
    
    Long getTotalPatients();
}
