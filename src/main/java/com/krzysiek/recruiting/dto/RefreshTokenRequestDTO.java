package com.krzysiek.recruiting.dto;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequestDTO(
        @NotBlank(message = "To regenerate token You need to send refreshToken")
        String refreshToken
) {}
