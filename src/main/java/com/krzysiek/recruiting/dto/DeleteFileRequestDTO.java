package com.krzysiek.recruiting.dto;

import com.krzysiek.recruiting.enums.FileType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DeleteFileRequestDTO(
        @NotBlank(message = "File id must be provided.")
        Long fileId,
        @NotNull(message = "File type must be provided.")
        FileType fileType
)
{}
