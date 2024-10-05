package com.krzysiek.recruiting.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ErrorResponseDTO {
    private final LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
    private String exceptionClassName;
    private String handlerClassName;
    @Setter
    private List<FieldErrorDTO> fieldErrors;

    public ErrorResponseDTO() {
        this.timestamp = LocalDateTime.now();
    }

    public ErrorResponseDTO(int status, String error, String message, String path, String exceptionClassName, String handlerClassName) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
        this.exceptionClassName = exceptionClassName;
        this.handlerClassName = handlerClassName;
    }

}
