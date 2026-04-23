package com.cts.healthconnect.appointment.exception;



public class AppointmentNotFoundException extends RuntimeException {

    public AppointmentNotFoundException(String appointmentCode) {
        super("Appointment not found with code: " + appointmentCode);
    }
}