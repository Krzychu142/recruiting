package com.krzysiek.recruiting.dto;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public class SaveFileRequest {
    @NotNull(message = "File can't be empty")
    private MultipartFile file;
}
