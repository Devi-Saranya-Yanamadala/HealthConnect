package com.cts.healthconnect.patient.exception;



public class PatientNotFoundException extends RuntimeException {

    public PatientNotFoundException(String patientCode) {
        super("Patient not found with code: " + patientCode);
    }
}
