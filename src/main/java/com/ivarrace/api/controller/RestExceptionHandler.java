package com.ivarrace.api.controller;

import com.ivarrace.api.service.NoContentException;
import com.ivarrace.api.service.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NoContentException.class)
    public ResponseEntity<List<Object>> noContentException(final NoContentException e) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> notFoundException(final NotFoundException e) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
