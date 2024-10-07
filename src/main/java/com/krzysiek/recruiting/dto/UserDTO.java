package com.krzysiek.recruiting.dto;

import java.util.Set;

public record UserDTO(
        Long id,
        String email,
        Boolean isConfirmed,
//        String confirmationToken, // ???
        Set<Long> fileIds,
        Set<Long> recruitmentProcessIds
) {}