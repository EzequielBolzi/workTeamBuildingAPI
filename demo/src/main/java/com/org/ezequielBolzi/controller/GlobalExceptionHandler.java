package com.org.ezequielBolzi.controller;

import com.org.ezequielBolzi.customException.CustomBadRequestException;
import com.org.ezequielBolzi.customException.CustomEmailAlreadyExistsException;
import com.org.ezequielBolzi.customException.CustomEmployeeNoteDeletableException;
import com.org.ezequielBolzi.customException.CustomInternalServerException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomBadRequestException.class)
    public ResponseEntity<String> handleCustomBadRequestException(CustomBadRequestException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(CustomInternalServerException.class)
    public ResponseEntity<String> handleCustomInternalServerException(CustomInternalServerException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    // Manejo de excepciones para EmailAlreadyExistsException
    @ExceptionHandler(CustomEmailAlreadyExistsException.class)
    public ResponseEntity<String> handleEmailAlreadyExistsException(CustomEmailAlreadyExistsException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
    @ExceptionHandler(CustomEmployeeNoteDeletableException.class)
    public ResponseEntity<String> handleEmployeeNoteDeletableException(CustomEmployeeNoteDeletableException ex) {
        return ResponseEntity.status(404).body(ex.getMessage());
    }
}
