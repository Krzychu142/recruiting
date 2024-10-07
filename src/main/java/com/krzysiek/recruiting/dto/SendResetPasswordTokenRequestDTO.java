package com.krzysiek.recruiting.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SendResetPasswordTokenRequestDTO(
        @NotBlank(message = "Email cant be empty.")
        @Email(message = "Email should be a valid email address.")
        String email
) {}
