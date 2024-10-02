package com.krzysiek.recruiting.exception;

import com.krzysiek.recruiting.dto.ErrorResponseDTO;
import com.krzysiek.recruiting.dto.FieldErrorDTO;
import com.krzysiek.recruiting.service.EmailService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;;
import java.util.List;


@ControllerAdvice
@ResponseBody
public class GlobalException {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(GlobalException.class);
    private final EmailService emailService;

    public GlobalException(EmailService emailService) {
        this.emailService = emailService;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest servletRequest) {
        String message = "An error occurred: " + ex.getMessage();
        log.error("MethodArgumentNotValidException occurred from global exception class.");

        List<FieldErrorDTO> allErrors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(fieldError -> new FieldErrorDTO(fieldError.getField(), fieldError.getDefaultMessage()))
                .toList();

        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO();
        errorResponseDTO.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorResponseDTO.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        errorResponseDTO.setPath(servletRequest.getRequestURI());
        errorResponseDTO.setMessage("Exception occurred from global exception class.");
        errorResponseDTO.setFieldErrors(allErrors);
        return new ResponseEntity<>(errorResponseDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    // request for more information about exception
    public ResponseEntity<ErrorResponseDTO> handleGlobalException(Exception ex, WebRequest request, HttpServletRequest servletRequest) {
        String message = "An error occurred: " + ex.getMessage();
        log.error("Exception occurred from global exception class.");
        emailService.sendErrorEmail(ex);

        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO();
        errorResponseDTO.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorResponseDTO.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        errorResponseDTO.setPath(servletRequest.getRequestURI());
        errorResponseDTO.setMessage("Exception occurred from global exception class.");
        return new ResponseEntity<>(errorResponseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
