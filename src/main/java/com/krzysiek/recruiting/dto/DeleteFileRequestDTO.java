package com.krzysiek.recruiting.dto;

import com.krzysiek.recruiting.enums.FileType;
import jakarta.validation.constraints.NotNull;

public record DeleteFileRequestDTO(
        @NotNull(message = "File id must be provided.")
        Long fileId,
        @NotNull(message = "File type must be provided.")
        FileType fileType
)
{}
