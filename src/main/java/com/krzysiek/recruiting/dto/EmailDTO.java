package com.krzysiek.recruiting.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record EmailDTO(
        @NotEmpty
        @Email
        String to,

        @Email
        String cc,

        @Email
        String bcc,

        @NotBlank
        String subject,

        @NotBlank
        String message
) {}
