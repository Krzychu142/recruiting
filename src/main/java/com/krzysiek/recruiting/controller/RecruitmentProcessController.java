package com.krzysiek.recruiting.controller;

import com.krzysiek.recruiting.service.RecruitmentProcessServiceImplementation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/recruitment")
public class RecruitmentProcessController {

    private final RecruitmentProcessServiceImplementation recruitmentProcessServiceImplementation;

    public RecruitmentProcessController(RecruitmentProcessServiceImplementation recruitmentProcessServiceImplementation) {
        this.recruitmentProcessServiceImplementation = recruitmentProcessServiceImplementation;
    }

    @PostMapping
    public ResponseEntity<?> createRecruitmentProcess(){
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
