package com.krzysiek.recruiting.controller;

import com.krzysiek.recruiting.dto.BaseResponseDTO;
import com.krzysiek.recruiting.dto.RegisterRequestDTO;
import com.krzysiek.recruiting.dto.RegisterResponseDTO;
import com.krzysiek.recruiting.exception.UserAlreadyExistsException;
import com.krzysiek.recruiting.service.AuthenticationService;
import io.jsonwebtoken.JwtException;
import jakarta.validation.Valid;
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

    @PostMapping("/login")
    public String login(){
        return "login";
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> register(@Valid @RequestBody RegisterRequestDTO request) throws RuntimeException {
        RegisterResponseDTO registerResponseDTO = authenticationService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(registerResponseDTO);
    }

    @GetMapping("/confirm-email")
    // When frontend will be implemented it should be moved from params into body of request.
    public ResponseEntity<BaseResponseDTO> confirmEmail(@RequestParam("token") String token) {
        BaseResponseDTO baseResponseDTO = authenticationService.confirmEmail(token);
        return ResponseEntity.status(HttpStatus.OK).body(baseResponseDTO);
    }

    //TODO: reset-password
    @PostMapping("/send-reset-password-link")
    public ResponseEntity<BaseResponseDTO> sendResetPasswordToken(@RequestBody String email){
        System.out.println(email);
        BaseResponseDTO baseResponseDTO = authenticationService.sendResetPasswordToken(email);
        return ResponseEntity.status(HttpStatus.OK).body(baseResponseDTO);
    }
    //TODO: logout

}
