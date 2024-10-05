package com.krzysiek.recruiting.exception;

import com.krzysiek.recruiting.dto.ErrorResponseDTO;
import com.krzysiek.recruiting.dto.FieldErrorDTO;
import com.krzysiek.recruiting.service.EmailService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import java.util.List;


@RestControllerAdvice
public class GlobalExceptionHandler extends BaseExceptionHandler{

    private final EmailService emailService;

    public GlobalExceptionHandler(EmailService emailService) {
        this.emailService = emailService;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest servletRequest) {

        List<FieldErrorDTO> allErrors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(fieldError -> new FieldErrorDTO(fieldError.getField(), fieldError.getDefaultMessage()))
                .toList();

        ErrorResponseDTO errorResponseDTO = getErrorResponseDTO(ex, servletRequest, HttpStatus.BAD_REQUEST);
        errorResponseDTO.setFieldErrors(allErrors);

        return handleException(errorResponseDTO);
    }

    @ExceptionHandler(Exception.class)
    // request for more information about exception
    public ResponseEntity<ErrorResponseDTO> handleGlobalException(Exception ex, WebRequest request, HttpServletRequest servletRequest) {
        emailService.sendErrorEmail(ex);
        return handleException(getErrorResponseDTO(ex, servletRequest, HttpStatus.INTERNAL_SERVER_ERROR));
    }
}
