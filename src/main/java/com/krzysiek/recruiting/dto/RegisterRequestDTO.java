package com.krzysiek.recruiting.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequestDTO(
        @NotBlank(message = "Email cant be empty.")
        @Email(message = "Email should be a valid email address.")
        String email,

        @NotBlank(message = "Password cant be empty.")
        @Size(min = 6, message = "Password must be at least 6 characters.")
        String password,

        @NotBlank(message = "Confirm password is required.")
        @Size(min = 6, message = "Confirm password must be at least 6 characters.")
        String confirmedPassword
) {

        // that was discovered by me after write my own NonIdenticalVariablesException and handled it in GlobalexceptionHandler, pure me
        @AssertTrue(message = "Passwords do not match.")
        public boolean isPasswordsMatching() {
                if (password == null || confirmedPassword == null) {
                        return false;
                }
                return password.equals(confirmedPassword);
        }
}
