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
    @Setter
    private List<FieldErrorDTO> fieldErrors;

    public ErrorResponseDTO() {
        this.timestamp = LocalDateTime.now();
    }

    public ErrorResponseDTO(int status, String error, String message, String path) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }

}
