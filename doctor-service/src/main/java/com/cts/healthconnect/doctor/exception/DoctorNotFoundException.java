package com.cts.healthconnect.doctor.exception;


public class DoctorNotFoundException extends RuntimeException {

    public DoctorNotFoundException(String doctorCode) {
        super("Doctor not found with code: " + doctorCode);
    }
}

