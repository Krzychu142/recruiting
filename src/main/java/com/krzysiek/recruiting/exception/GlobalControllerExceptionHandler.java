package com.krzysiek.recruiting.exception;

import com.krzysiek.recruiting.dto.responsDTOs.ErrorResponseDTO;
import com.krzysiek.recruiting.dto.FieldErrorDTO;
import com.krzysiek.recruiting.exception.customExceptions.*;
import com.krzysiek.recruiting.service.IEmailService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;


@RestControllerAdvice
public class GlobalControllerExceptionHandler extends BaseExceptionHandler{

    private final IEmailService emailService;

    public GlobalControllerExceptionHandler(IEmailService emailService) {
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
        return handleException(getErrorResponseDTO(ex, servletRequest, HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponseDTO> handleIllegalStateException(IllegalStateException ex, HttpServletRequest servletRequest){
        return handleException(getErrorResponseDTO(ex, servletRequest, HttpStatus.UNAUTHORIZED));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDTO> handleIllegalArgumentException(IllegalArgumentException ex, HttpServletRequest servletRequest){
        return handleException(getErrorResponseDTO(ex, servletRequest, HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(JobDescriptionNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleNotFoundException(JobDescriptionNotFoundException ex, HttpServletRequest servletRequest){
        return handleException(getErrorResponseDTO(ex, servletRequest, HttpStatus.NOT_FOUND));
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponseDTO> handleMaxSizeExceededException(MaxUploadSizeExceededException ex, HttpServletRequest servletRequest){
        return handleException(getErrorResponseDTO(ex, servletRequest, HttpStatus.PAYLOAD_TOO_LARGE ));
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorResponseDTO> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException ex, HttpServletRequest servletRequest){
        return handleException(getErrorResponseDTO(ex, servletRequest, HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponseDTO> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex, HttpServletRequest servletRequest){
        return handleException(getErrorResponseDTO(ex, servletRequest, HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponseDTO> handleMissingServletRequestParameterException(MissingServletRequestParameterException ex, HttpServletRequest servletRequest){
        return handleException(getErrorResponseDTO(ex, servletRequest, HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(StorageException.class)
    public ResponseEntity<ErrorResponseDTO> handleStorageException(StorageException ex, HttpServletRequest servletRequest){
        return handleException(getErrorResponseDTO(ex, servletRequest, HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleStorageFileNotFoundException(StorageFileNotFoundException ex, HttpServletRequest servletRequest){
        return handleException(getErrorResponseDTO(ex, servletRequest, HttpStatus.NOT_FOUND));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponseDTO> handleAccessDeniedException(AccessDeniedException ex, HttpServletRequest servletRequest) {
        HttpStatus status = resolveResponseStatus(ex);
        return handleException(getErrorResponseDTO(ex, servletRequest, status));
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<ErrorResponseDTO> handleMissingServletRequestPartException(MissingServletRequestPartException ex, HttpServletRequest servletRequest) {
        return handleException(getErrorResponseDTO(ex, servletRequest, HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<ErrorResponseDTO> handleMultipartException(MultipartException ex, HttpServletRequest servletRequest) {
        return handleException(getErrorResponseDTO(ex, servletRequest, HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleNoResourceFoundException(NoResourceFoundException ex, HttpServletRequest servletRequest) {
        return handleException(getErrorResponseDTO(ex, servletRequest, HttpStatus.NOT_FOUND));
    }

    @ExceptionHandler(JobDescriptionAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDTO> handleJobDescriptionAlreadyExistsException(JobDescriptionAlreadyExistsException ex, HttpServletRequest servletRequest) {
        return handleException(getErrorResponseDTO(ex, servletRequest, HttpStatus.CONFLICT));
    }

    @ExceptionHandler(RecruitmentProcessNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleRecruitmentProcessNotFoundException(RecruitmentProcessNotFoundException ex, HttpServletRequest servletRequest) {
        return handleException(getErrorResponseDTO(ex, servletRequest, HttpStatus.NOT_FOUND));
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDTO> handleUserAlreadyExistsException(UserAlreadyExistsException ex, HttpServletRequest servletRequest) {
        return handleException(getErrorResponseDTO(ex, servletRequest, HttpStatus.CONFLICT));
    }

    @ExceptionHandler(Exception.class)
    // request for more information about exception
    public ResponseEntity<ErrorResponseDTO> handleGlobalException(Exception ex, WebRequest request, HttpServletRequest servletRequest) {
        emailService.sendErrorEmail(ex);
        return handleException(getErrorResponseDTO(ex, servletRequest, HttpStatus.INTERNAL_SERVER_ERROR));
    }

    private HttpStatus resolveResponseStatus(Exception ex) {
        ResponseStatus responseStatus = ex.getClass().getAnnotation(ResponseStatus.class);
        return (responseStatus != null) ? responseStatus.value() : HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
