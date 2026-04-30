package com.cts.healthconnect.auth.exception;

import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidCredentials(
            InvalidCredentialsException ex) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", 401);
        body.put("error", "Unauthorized");
        body.put("message", ex.getMessage());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
    }
    

    @ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, Object>> handleValidationErrors(
	        MethodArgumentNotValidException ex) {
	
	    Map<String, String> errors = new HashMap<>();
	    ex.getBindingResult().getFieldErrors()
	        .forEach(error ->
	            errors.put(error.getField(), error.getDefaultMessage())
	        );
	
	    Map<String, Object> body = new LinkedHashMap<>();
	    body.put("timestamp", LocalDateTime.now());
	    body.put("status", 400);
	    body.put("error", "Bad Request");
	    body.put("messages", errors);
	
	    return ResponseEntity.badRequest().body(body);
	}

}

