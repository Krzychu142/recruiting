package com.krzysiek.recruiting.dto;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record SaveFileRequestDTO(
        @NotNull(message = "File can't be empty")
        MultipartFile file
){}
