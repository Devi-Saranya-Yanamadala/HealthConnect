package com.cts.healthconnect.doctor.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DoctorNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleDoctorNotFound(
            DoctorNotFoundException ex) {

        Map<String, Object> error = new LinkedHashMap<>();
        error.put("timestamp", LocalDateTime.now());
        error.put("status", HttpStatus.NOT_FOUND.value());
        error.put("error", "Doctor Not Found");
        error.put("message", ex.getMessage());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
    

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, Object>> handleValidationErrors(
	        MethodArgumentNotValidException ex) {
	
	    Map<String, String> fieldErrors = new LinkedHashMap<>();
	
	    ex.getBindingResult().getFieldErrors()
	        .forEach(error ->
	            fieldErrors.put(error.getField(), error.getDefaultMessage())
	        );
	
	    Map<String, Object> body = new LinkedHashMap<>();
	    body.put("timestamp", LocalDateTime.now());
	    body.put("status", HttpStatus.BAD_REQUEST.value());
	    body.put("error", "Bad Request");
	    body.put("messages", fieldErrors);
	
	    return ResponseEntity.badRequest().body(body);
	}


    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(
            Exception ex) {

        Map<String, Object> error = new LinkedHashMap<>();
        error.put("timestamp", LocalDateTime.now());
        error.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        error.put("error", "Internal Server Error");
        error.put("message", ex.getMessage());

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

