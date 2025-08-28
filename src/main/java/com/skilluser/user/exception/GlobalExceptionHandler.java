package com.skilluser.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handle(Exception e){
        return ResponseEntity.status(200).body(Map.of("Msg",e.getMessage(),"status", HttpStatus.INTERNAL_SERVER_ERROR));
    }
}
