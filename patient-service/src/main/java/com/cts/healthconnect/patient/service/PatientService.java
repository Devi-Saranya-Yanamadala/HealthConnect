package com.cts.healthconnect.patient.service;



import java.util.List;

import com.cts.healthconnect.patient.dto.*;

public interface PatientService {

    PatientResponseDto registerPatient(PatientRequestDto dto);

    PatientResponseDto getPatientByCode(String patientCode);

    void deactivatePatient(String patientCode);

    PatientResponseDto markPatientDeceased(String patientCode);
    
    Long getTotalPatients();
    Long getPatientCountByDate(String date);

	void activatePatient(String patientCode);

	List<PatientResponseDto> getAllPatients();
}
