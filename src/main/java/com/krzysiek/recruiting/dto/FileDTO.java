package com.krzysiek.recruiting.dto;

import com.krzysiek.recruiting.enums.FileType;

public record FileDTO(
        Long id,
        Long userId,
        FileType fileType,
        String link
) {}