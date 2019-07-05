package com.emprovise.service.data.exception.handler;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({Exception.class})
    protected Object handleInvalidRequest(Exception exception) {
        exception.printStackTrace();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String serviceResponse = "Error: " + exception.getMessage();
        return new ResponseEntity<Object>(serviceResponse, headers, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
