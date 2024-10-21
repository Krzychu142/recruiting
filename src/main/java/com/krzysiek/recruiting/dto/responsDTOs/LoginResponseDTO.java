package com.krzysiek.recruiting.dto.responsDTOs;

import lombok.Getter;


@Getter
public class LoginResponseDTO extends BaseResponseDTO {
        private final String accessToken;
        private final String refreshToken;

        public LoginResponseDTO(String accessToken, String refreshToken, String message) {
                super(message);
                this.accessToken = accessToken;
                this.refreshToken = refreshToken;
        }
}
