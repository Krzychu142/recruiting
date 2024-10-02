package com.krzysiek.recruiting.controller;

import com.krzysiek.recruiting.dto.RegisterRequestDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
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
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequestDTO request){
//        if (bindingResult.hasErrors()){
////            bindingResult.getFieldError()
//            return new ResponseEntity<>(bindingResult.getFieldError(), HttpStatus.BAD_REQUEST);
//        }

        System.out.println(request);
        return new ResponseEntity<>("register", HttpStatus.OK);
    }
}
