package com.krzysiek.recruiting.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequestDTO(
        @NotBlank(message = "Email cant be empty.")
        @Email(message = "Email should be a valid email address.")
        String email,

        @NotBlank(message = "Password cant be empty.")
        @Size(min = 6, message = "Password must be at least 6 characters.")
        String password,

        @NotBlank(message = "Confirm password is required.")
        String confirmedPassword
) {}
