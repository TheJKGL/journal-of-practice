package com.malakhov.journalofpractice.advice;

import com.malakhov.journalofpractice.exception.ExceptionResponse;
import com.malakhov.journalofpractice.exception.ResourceAlreadyExistException;
import com.malakhov.journalofpractice.exception.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {AuthenticationException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    protected ExceptionResponse handleException(AuthenticationException e, HttpServletRequest request) {
        return new ExceptionResponse(e.getMessage(), request.getServletPath());
    }

    @ExceptionHandler(value = {ResourceNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected ExceptionResponse handleException(ResourceNotFoundException e, HttpServletRequest request) {
        return new ExceptionResponse(e.getMessage(), request.getServletPath());
    }

    @ExceptionHandler(value = {ResourceAlreadyExistException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ExceptionResponse handleException(ResourceAlreadyExistException e, HttpServletRequest request) {
        return new ExceptionResponse(e.getMessage(), request.getServletPath());
    }

    /*@ExceptionHandler(AccessDeniedException.class)
    public ExceptionResponse handleValidationException(AccessDeniedException e) {
        return new ExceptionResponse()
        return ResponseEntity.status(401).body("{\"status\":\"FAILED\", \"reason\": \"Unauthorized\"}");
    }*/
}
