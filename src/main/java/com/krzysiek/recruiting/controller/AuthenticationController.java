package com.krzysiek.recruiting.controller;

import com.krzysiek.recruiting.dto.*;
import com.krzysiek.recruiting.service.AuthenticationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/authentication")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService){
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<BaseResponseDTO> register(@Valid @RequestBody RegisterRequestDTO request){
        BaseResponseDTO baseResponseDTO = authenticationService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(baseResponseDTO);
    }

    @PostMapping("/confirm-email")
    // When frontend will be implemented it should be moved from params into body of request.
    public ResponseEntity<BaseResponseDTO> confirmEmail(@RequestParam("token") String token) {
        BaseResponseDTO baseResponseDTO = authenticationService.confirmEmail(token);
        return ResponseEntity.status(HttpStatus.OK).body(baseResponseDTO);
    }

    @PostMapping("/send-reset-password-link")
    public ResponseEntity<BaseResponseDTO> sendResetPasswordToken(@RequestBody SendResetPasswordTokenRequestDTO sendResetPasswordTokenRequestDTO){
        BaseResponseDTO baseResponseDTO = authenticationService.sendResetPasswordToken(sendResetPasswordTokenRequestDTO.email());
        return ResponseEntity.status(HttpStatus.OK).body(baseResponseDTO);
    }

    @PostMapping("/reset-password")
    // In final project token should be moved into body of request.
    public ResponseEntity<BaseResponseDTO> resetPassword(@RequestParam("token") String token, @Valid @RequestBody ResetPasswordRequestDTO resetPasswordRequestDTO){
        BaseResponseDTO baseResponseDTO = authenticationService.resetPassword(token, resetPasswordRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(baseResponseDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO){
        LoginResponseDTO loginResponseDTO = authenticationService.login(loginRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(loginResponseDTO);
    }

    //TODO: logout
    //TODO: refresh-token
}
