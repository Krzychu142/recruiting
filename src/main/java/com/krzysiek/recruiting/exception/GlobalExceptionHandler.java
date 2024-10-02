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
import java.util.ArrayList;
import java.util.List;


@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private final EmailService emailService;

    public GlobalExceptionHandler(EmailService emailService) {
        this.emailService = emailService;
    }

    @ExceptionHandler(NonIdenticalVariablesException.class)
    public ResponseEntity<ErrorResponseDTO> handleNonIdenticalVariablesException(NonIdenticalVariablesException ex, HttpServletRequest servletRequest){
        log.error("NonIdenticalVariablesException occurred from global exception class.");

        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO();
        errorResponseDTO.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponseDTO.setError(HttpStatus.BAD_REQUEST.getReasonPhrase());
        errorResponseDTO.setPath(servletRequest.getRequestURI());
        errorResponseDTO.setMessage(ex.getMessage());

        List<FieldErrorDTO> fieldErrorDTOList = new ArrayList<>();
        fieldErrorDTOList.add(ex.getFieldErrorDTO());

        errorResponseDTO.setFieldErrors(fieldErrorDTOList);
        return new ResponseEntity<>(errorResponseDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest servletRequest) {
        log.error("MethodArgumentNotValidException occurred from global exception class.");

        List<FieldErrorDTO> allErrors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(fieldError -> new FieldErrorDTO(fieldError.getField(), fieldError.getDefaultMessage()))
                .toList();

        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO();
        errorResponseDTO.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponseDTO.setError(HttpStatus.BAD_REQUEST.getReasonPhrase());
        errorResponseDTO.setPath(servletRequest.getRequestURI());
        errorResponseDTO.setMessage("Exception occurred from global exception class.");
        errorResponseDTO.setFieldErrors(allErrors);
        return new ResponseEntity<>(errorResponseDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    // request for more information about exception
    public ResponseEntity<ErrorResponseDTO> handleGlobalException(Exception ex, WebRequest request, HttpServletRequest servletRequest) {
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
