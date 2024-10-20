package com.krzysiek.recruiting.controller;

import com.krzysiek.recruiting.dto.AllRecruitmentProcessesResponseDTO;
import com.krzysiek.recruiting.dto.BaseResponseDTO;
import com.krzysiek.recruiting.dto.RecruitmentProcessRequestDTO;
import com.krzysiek.recruiting.service.RecruitmentProcessServiceImplementation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
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
    public ResponseEntity<BaseResponseDTO> createRecruitmentProcess(@Valid @RequestBody RecruitmentProcessRequestDTO recruitmentProcessRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(recruitmentProcessService.createRecruitmentProcess(recruitmentProcessRequestDTO));
    }

    @GetMapping
    public ResponseEntity<AllRecruitmentProcessesResponseDTO> getAllRecruitmentProcesses(@RequestParam(name = "page-number", defaultValue = "0") @Min(0) int pageNumber) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new AllRecruitmentProcessesResponseDTO("All user's recruitment process list. Page: " + pageNumber + ".",
                        recruitmentProcessService.getAllRecruitmentProcesses(pageNumber))
        );
    }
}
