package com.krzysiek.recruiting.exception;

import com.krzysiek.recruiting.service.EmailService;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalException {

    private final EmailService emailService;

    public GlobalException(EmailService emailService) {
        this.emailService = emailService;
    }

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(GlobalException.class);
    @ExceptionHandler(Exception.class)
    // request for more information about exception
    public ResponseEntity<Object> handleGlobalException(Exception ex, WebRequest request) {
        String message = "An error occurred: " + ex.getMessage();
        log.error("Exception occurred from global exception class.");
        emailService.sendErrorEmail(ex);
        return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
