package com.krzysiek.recruiting.dto;

import lombok.Getter;

@Getter
public class BaseResponseDTO {

    private final String message;

    public BaseResponseDTO(String message) {
        this.message = message;
    }
}
