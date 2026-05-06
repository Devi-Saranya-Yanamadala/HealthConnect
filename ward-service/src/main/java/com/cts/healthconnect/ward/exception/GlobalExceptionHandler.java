package com.cts.healthconnect.ward.exception;



import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AdmissionNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleAdmissionNotFound(
            AdmissionNotFoundException ex) {

        return error(HttpStatus.NOT_FOUND, "Admission Not Found", ex.getMessage());
    }

    @ExceptionHandler(BedNotAvailableException.class)
    public ResponseEntity<Map<String, Object>> handleBedError(
            BedNotAvailableException ex) {

        return error(HttpStatus.BAD_REQUEST, "Bed Not Available", ex.getMessage());
    }
    

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, Object>> handleValidationErrors(
	        MethodArgumentNotValidException ex) {
	
	    Map<String, String> errors = new LinkedHashMap<>();
	
	    ex.getBindingResult().getFieldErrors()
	        .forEach(error ->
	            errors.put(error.getField(), error.getDefaultMessage())
	        );
	
	    Map<String, Object> body = new LinkedHashMap<>();
	    body.put("timestamp", LocalDateTime.now());
	    body.put("status", HttpStatus.BAD_REQUEST.value());
	    body.put("error", "Bad Request");
	    body.put("messages", errors);
	
	    return ResponseEntity.badRequest().body(body);
	}


    private ResponseEntity<Map<String, Object>> error(
            HttpStatus status, String error, String message) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", error);
        body.put("message", message);

        return new ResponseEntity<>(body, status);
    }
}