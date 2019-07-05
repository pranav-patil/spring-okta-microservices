package com.emprovise.service.edge.exception.handler;

import com.emprovise.service.edge.response.MessageSeverity;
import com.emprovise.service.edge.response.ServiceResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.UUID;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({Exception.class})
    protected Object handleInvalidRequest(Exception exception) {
        exception.printStackTrace();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ServiceResponse serviceResponse = getServiceResponse(exception, UUID.randomUUID().toString());
        return new ResponseEntity<Object>(serviceResponse, headers, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ServiceResponse getServiceResponse(Exception exception, String errorID) {
        ServiceResponse serviceResponse = new ServiceResponse();
        serviceResponse.setMessage(exception.getMessage());
        serviceResponse.setSeverity(MessageSeverity.ERROR);
        serviceResponse.setMessageId(errorID);
        return serviceResponse;
    }
}
