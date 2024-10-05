package com.krzysiek.recruiting.exception;

import com.krzysiek.recruiting.dto.ErrorResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class AuthenticationServiceExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDTO>  handleUserAlreadyExistsException(UserAlreadyExistsException ex, HttpServletRequest servletRequest){
        // TODO: repeated segments should be moved into constructor - this fields will be for sure, only list of fields should be optional - setters
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO();
        errorResponseDTO.setStatus(HttpStatus.CONFLICT.value());
        errorResponseDTO.setError(HttpStatus.BAD_REQUEST.getReasonPhrase());
        errorResponseDTO.setMessage("Exception occurred from AuthenticationServiceExceptionHandler. " + ex.getMessage());
        errorResponseDTO.setPath(servletRequest.getRequestURI());
        return new ResponseEntity<>(errorResponseDTO, HttpStatus.CONFLICT);
    }

}
