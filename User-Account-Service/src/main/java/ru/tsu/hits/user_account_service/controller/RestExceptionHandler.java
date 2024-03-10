package ru.tsu.hits.user_account_service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.tsu.hits.user_account_service.exception.UserNotFoundException;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(value = { UserNotFoundException.class })
    protected ResponseEntity<Object> handleNotFound(Exception ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(value = { Exception.class })
    public ResponseEntity<Object> handleAll(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }

    // Define more exception handlers as needed
}
