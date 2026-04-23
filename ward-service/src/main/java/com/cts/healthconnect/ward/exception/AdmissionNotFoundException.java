package com.cts.healthconnect.ward.exception;

public class AdmissionNotFoundException extends RuntimeException {
    public AdmissionNotFoundException(String code) {
        super("Admission not found with code: " + code);
    }
}
