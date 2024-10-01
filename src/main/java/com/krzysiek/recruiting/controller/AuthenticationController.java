package com.krzysiek.recruiting.controller;

import com.krzysiek.recruiting.dto.RegisterRequestDTO;
import com.krzysiek.recruiting.validator.RegisterRequestValidator;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/authentication")
public class AuthenticationController {

    private final RegisterRequestValidator registerRequestValidator;

    public AuthenticationController(RegisterRequestValidator registerRequestValidator) {
        this.registerRequestValidator = registerRequestValidator;
    }

    @PostMapping("/login")
    public String login(){
        return "login";
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequestDTO request, BindingResult bindingResult){
        registerRequestValidator.validate(request, bindingResult);

        if (bindingResult.hasErrors()){
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }

        System.out.println(request);
        return new ResponseEntity<>("register", HttpStatus.OK);
    }

}
