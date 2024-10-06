package com.krzysiek.recruiting.dto;

import jakarta.validation.constraints.NotBlank;

public record RegisterResponseDTO(
        @NotBlank
        Long userId,

        @NotBlank
        String message
) {}
