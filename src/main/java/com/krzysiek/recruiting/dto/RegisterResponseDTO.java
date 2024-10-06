package com.krzysiek.recruiting.dto;

import lombok.Getter;


@Getter
public class RegisterResponseDTO extends BaseResponseDTO {
        private final Long userId;

        public RegisterResponseDTO(Long userId, String message) {
                super(message);
                this.userId = userId;
        }
}
