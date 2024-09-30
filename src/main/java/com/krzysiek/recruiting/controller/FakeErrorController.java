package com.krzysiek.recruiting.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FakeErrorController {

    @GetMapping("/error")
    public String generateError(){
        throw new RuntimeException("Test error to trigger email.");
    }
}
