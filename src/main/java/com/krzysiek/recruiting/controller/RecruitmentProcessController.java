package com.krzysiek.recruiting.controller;

import com.krzysiek.recruiting.dto.responsDTOs.AllRecruitmentProcessesResponseDTO;
import com.krzysiek.recruiting.dto.responsDTOs.BaseResponseDTO;
import com.krzysiek.recruiting.dto.requestDTOs.RecruitmentProcessRequestDTO;
import com.krzysiek.recruiting.enums.RecruitmentProcessStatus;
import com.krzysiek.recruiting.service.RecruitmentProcessServiceImplementation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
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
            @RequestParam(name = "sort-direction", defaultValue = "ASC") @Pattern(regexp = "ASC|DESC", message = "Sort direction must be 'ASC' or 'DESC'.") String sortDirection,
            @RequestParam(name = "status", required = false) RecruitmentProcessStatus status,
            @RequestParam(name = "cv-id", required = false) @Min(value = 1, message = "CV id must be greater than 0.") Long cvId
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new AllRecruitmentProcessesResponseDTO("All user's recruitment process list. Page: " + pageNumber + ".",
                        recruitmentProcessService.getAllRecruitmentProcesses(pageNumber, sortBy, sortDirection))
        );
    }
}
