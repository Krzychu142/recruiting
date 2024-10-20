package com.krzysiek.recruiting.controller;

import com.krzysiek.recruiting.dto.BaseResponseDTO;
import com.krzysiek.recruiting.dto.CreateRecruitmentProcessRequestDTO;
import com.krzysiek.recruiting.service.RecruitmentProcessServiceImplementation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/recruitment")
public class RecruitmentProcessController {

    private final RecruitmentProcessServiceImplementation recruitmentProcessService;

    public RecruitmentProcessController(RecruitmentProcessServiceImplementation recruitmentProcessServiceImplementation) {
        this.recruitmentProcessService = recruitmentProcessServiceImplementation;
    }

    @PostMapping
    public ResponseEntity<BaseResponseDTO> createRecruitmentProcess(@Valid @RequestBody CreateRecruitmentProcessRequestDTO recruitmentProcessRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(recruitmentProcessService.createRecruitmentProcess(recruitmentProcessRequestDTO));
    }

    @GetMapping
    public ResponseEntity<BaseResponseDTO> getAllRecruitmentProcesses() {
        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponseDTO("getAllRecruitmentProcesses"));
    }
}
