package com.krzysiek.recruiting.controller;

import com.krzysiek.recruiting.dto.responsDTOs.AllRecruitmentProcessesResponseDTO;
import com.krzysiek.recruiting.dto.responsDTOs.BaseResponseDTO;
import com.krzysiek.recruiting.dto.requestDTOs.RecruitmentProcessRequestDTO;
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
    public ResponseEntity<AllRecruitmentProcessesResponseDTO> getAllRecruitmentProcesses(
            @RequestParam(name = "page-number", defaultValue = "0") @Min(0) int pageNumber,
            @RequestParam(name = "sort-by", defaultValue = "dateOfApplication") String sortBy,
            @RequestParam(name = "sort-direction", defaultValue = "ASC") String sortDirection
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new AllRecruitmentProcessesResponseDTO("All user's recruitment process list. Page: " + pageNumber + ".",
                        recruitmentProcessService.getAllRecruitmentProcesses(pageNumber, sortBy, sortDirection))
        );
    }
}
