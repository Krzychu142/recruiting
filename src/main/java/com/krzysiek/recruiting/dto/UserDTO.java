package com.krzysiek.recruiting.dto;

import com.krzysiek.recruiting.model.OAuthProvider;
import java.util.Set;

public record UserDTO(
        Long id,
        String email,
        OAuthProvider oauthProvider,
        String oauthId,
        Set<Long> fileIds,
        Set<Long> recruitmentProcessIds
) {}