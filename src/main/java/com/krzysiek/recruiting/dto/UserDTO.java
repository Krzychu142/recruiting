package com.krzysiek.recruiting.dto;

import java.util.Set;

public record UserDTO(
        Long id,
        String email,
        Set<Long> fileIds,
        Set<Long> recruitmentProcessIds
) {}