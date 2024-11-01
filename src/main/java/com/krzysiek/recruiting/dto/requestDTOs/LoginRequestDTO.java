package com.krzysiek.recruiting.dto.requestDTOs;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(
        @NotBlank(message = "Email cant be empty.")
        @Email(message = "Email should be a valid email address.")
        String email,

        @NotBlank(message = "Password can't be empty")
        String password
){}
