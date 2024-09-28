package com.krzysiek.recruiting.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RecruitmentProcessController {

    @GetMapping("/recruitment")
    public String getAllRecruitment(){
        return "Test";
    }
}
