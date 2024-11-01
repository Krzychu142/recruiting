package com.krzysiek.recruiting.controller;

import com.krzysiek.recruiting.dto.responsDTOs.AllRecruitmentProcessesResponseDTO;
import com.krzysiek.recruiting.dto.requestDTOs.RecruitmentProcessRequestDTO;
import com.krzysiek.recruiting.dto.responsDTOs.BaseResponseDTO;
import com.krzysiek.recruiting.dto.responsDTOs.RecruitmentProcessResponseDTO;
import com.krzysiek.recruiting.enums.RecruitmentProcessStatus;
import com.krzysiek.recruiting.service.IRecruitmentProcessService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/recruitment")
public class RecruitmentProcessController {

    private final IRecruitmentProcessService recruitmentProcessService;

    public RecruitmentProcessController(IRecruitmentProcessService recruitmentProcessService) {
        this.recruitmentProcessService = recruitmentProcessService;
    }

    @PostMapping
    public ResponseEntity<RecruitmentProcessResponseDTO> createRecruitmentProcess(@Valid @RequestBody RecruitmentProcessRequestDTO recruitmentProcessRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new RecruitmentProcessResponseDTO("Successfully created recruitment process.",
                        recruitmentProcessService.createRecruitmentProcess(recruitmentProcessRequestDTO))
                );
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
                        recruitmentProcessService.getAllRecruitmentProcesses(pageNumber, sortBy, sortDirection, status, cvId))
        );
    }

    @PutMapping
    public ResponseEntity<RecruitmentProcessResponseDTO> editRecruitmentProcess(@Valid @RequestBody RecruitmentProcessRequestDTO recruitmentProcessRequestDTO){
        return ResponseEntity.status(HttpStatus.OK).body(
                new RecruitmentProcessResponseDTO("Successfully edited recruitment process.",
                        recruitmentProcessService.updateRecruitmentProcess(recruitmentProcessRequestDTO))
        );
    }

    @DeleteMapping
    public ResponseEntity<BaseResponseDTO> deleteRecruitmentProcess(@RequestParam(name = "id") @Min(0) Long recruitmentProcessId){
        // or simpler - 204 and no content
        recruitmentProcessService.deleteRecruitmentProcess(recruitmentProcessId);
        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponseDTO("Successfully deleted recruitment process."));
    }
}
