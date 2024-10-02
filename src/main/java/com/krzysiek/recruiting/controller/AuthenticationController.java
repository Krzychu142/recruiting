package com.krzysiek.recruiting.controller;

import com.krzysiek.recruiting.dto.FieldErrorDTO;
import com.krzysiek.recruiting.dto.RegisterRequestDTO;
import com.krzysiek.recruiting.exception.NonIdenticalVariablesException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/authentication")
public class AuthenticationController {

    @PostMapping("/login")
    public String login(){
        return "login";
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequestDTO request) throws NonIdenticalVariablesException {
        if (!request.password().equals(request.confirmedPassword())) {
            throw new NonIdenticalVariablesException("Password and confirmed password are different.", new FieldErrorDTO("confirmedPassword", "Confirmed password and password must be the same."));
        }

        return new ResponseEntity<>("register", HttpStatus.OK);
    }
}
