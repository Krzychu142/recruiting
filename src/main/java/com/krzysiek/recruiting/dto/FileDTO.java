package com.krzysiek.recruiting.dto;

import com.krzysiek.recruiting.model.FileType;

public record FileDTO(
        Long id,
        Long userId,
        FileType fileType,
        String link
) {}