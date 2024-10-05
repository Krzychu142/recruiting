package com.krzysiek.recruiting.exception;

import com.krzysiek.recruiting.dto.ErrorResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

public class BaseExceptionHandler {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(BaseExceptionHandler.class);

    protected ResponseEntity<ErrorResponseDTO> handleException(ErrorResponseDTO errorResponseDTO) {
        log.error("\nException occurred:\nExceptionClassName: {}\nHandlerClassName: {}\nErrorMessage: {}\nPath: {}",
                errorResponseDTO.getExceptionClassName(), errorResponseDTO.getHandlerClassName(), errorResponseDTO.getMessage(), errorResponseDTO.getPath());
        return new ResponseEntity<>(errorResponseDTO, HttpStatusCode.valueOf(errorResponseDTO.getStatus()));
    }

    protected ErrorResponseDTO getErrorResponseDTO(Exception ex, HttpServletRequest servletRequest, HttpStatus status){
        return new ErrorResponseDTO(
                status.value(),
                status.getReasonPhrase(),
                ex.getMessage(),
                servletRequest.getRequestURI(),
                ex.getClass().getSimpleName(),
                this.getClass().getSimpleName()
        );
    }

}
