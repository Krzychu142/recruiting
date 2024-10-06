package com.krzysiek.recruiting.exception;

import com.krzysiek.recruiting.dto.ErrorResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AuthenticationServiceExceptionHandler extends BaseExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDTO>  handleUserAlreadyExistsException(UserAlreadyExistsException ex, HttpServletRequest servletRequest){
        return handleException(getErrorResponseDTO(ex, servletRequest, HttpStatus.CONFLICT));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleUserNotFoundException(UserNotFoundException ex, HttpServletRequest servletRequest){
        return handleException(getErrorResponseDTO(ex, servletRequest, HttpStatus.NOT_FOUND));
    }

}
