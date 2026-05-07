package com.cts.healthconnect.appointment.exception;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AppointmentNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(
            AppointmentNotFoundException ex) {
        return build(HttpStatus.NOT_FOUND, "Appointment Not Found", ex.getMessage());
    }

    // ✅ FIXED: IllegalStateException returns 409 Conflict not 400
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, Object>> handleConflict(
            IllegalStateException ex) {
        return build(HttpStatus.CONFLICT, "Conflict", ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(Exception ex) {
        // ✅ Log the real error so you can debug in IDE console
        System.err.println(">>> APPOINTMENT ERROR: " + ex.getClass().getName()
            + " — " + ex.getMessage());
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Error", ex.getMessage());
    }

    private ResponseEntity<Map<String, Object>> build(
            HttpStatus status, String error, String message) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", error);
        body.put("message", message);
        return new ResponseEntity<>(body, status);
    }
}