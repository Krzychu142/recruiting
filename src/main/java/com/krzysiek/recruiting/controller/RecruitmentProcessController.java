package com.krzysiek.recruiting.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/recruitment")
public class RecruitmentProcessController {

    @GetMapping()
    public String getAllRecruitment(){
        return "Test";
    }
}
