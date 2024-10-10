package com.krzysiek.recruiting.exception;

import com.krzysiek.recruiting.dto.ErrorResponseDTO;
import com.krzysiek.recruiting.dto.FieldErrorDTO;
import com.krzysiek.recruiting.service.EmailService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import java.util.List;


@RestControllerAdvice
public class GlobalControllerExceptionHandler extends BaseExceptionHandler{

    private final EmailService emailService;

    public GlobalControllerExceptionHandler(EmailService emailService) {
        this.emailService = emailService;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handelMethodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest servletRequest) {

        List<FieldErrorDTO> allErrors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(fieldError -> new FieldErrorDTO(fieldError.getField(), fieldError.getDefaultMessage()))
                .toList();

        ErrorResponseDTO errorResponseDTO = getErrorResponseDTO(ex, servletRequest, HttpStatus.BAD_REQUEST);
        errorResponseDTO.setFieldErrors(allErrors);

        return handleException(errorResponseDTO);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponseDTO> handelValidationException(ValidationException ex, HttpServletRequest servletRequest){
        ErrorResponseDTO errorResponseDTO = getErrorResponseDTO(ex, servletRequest, HttpStatus.BAD_REQUEST);
        return handleException(errorResponseDTO);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ErrorResponseDTO> handleJwtException(JwtException ex, HttpServletRequest servletRequest){
        return handleException(getErrorResponseDTO(ex, servletRequest, HttpStatus.UNAUTHORIZED));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponseDTO> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex, HttpServletRequest servletRequest){
        return handleException(getErrorResponseDTO(ex, servletRequest, HttpStatus.METHOD_NOT_ALLOWED));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseDTO> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex, HttpServletRequest servletRequest){
        return handleException(getErrorResponseDTO(ex, servletRequest, HttpStatus.METHOD_NOT_ALLOWED));
    }

    @ExceptionHandler(Exception.class)
    // request for more information about exception
    public ResponseEntity<ErrorResponseDTO> handleGlobalException(Exception ex, WebRequest request, HttpServletRequest servletRequest) {
        emailService.sendErrorEmail(ex);
        return handleException(getErrorResponseDTO(ex, servletRequest, HttpStatus.INTERNAL_SERVER_ERROR));
    }
}
