package com.zilmar.springsecurity.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Object> handleResponseStatusException(ResponseStatusException ex) {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("status", ex.getStatusCode().value());
        responseBody.put("error", ex.getStatusCode());
        responseBody.put("message", ex.getReason());

        return ResponseEntity.status(ex.getStatusCode()).body(responseBody);
    }
}
