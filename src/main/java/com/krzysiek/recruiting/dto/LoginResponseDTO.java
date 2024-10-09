package com.krzysiek.recruiting.dto;

import lombok.Getter;


@Getter
public class LoginResponseDTO extends BaseResponseDTO {
        private final String token;

        public LoginResponseDTO(String token, String message) {
                super(message);
                this.token = token;
        }
}
