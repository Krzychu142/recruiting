package com.krzysiek.recruiting.dto;

import com.krzysiek.recruiting.enums.RecruitmentProcessStatus;
import com.krzysiek.recruiting.enums.RecruitmentTaskStatus;
import jakarta.validation.Valid;

import java.time.LocalDate;

public record CreateRecruitmentProcessRequestDTO(
        @Valid
        JobDescriptionDTO jobDescriptionDTO,
        Long cvId,
        Long recruitmentTaskId,
        LocalDate dateOfApplication,
        LocalDate processEndDate,
        Boolean hasRecruitmentTask,
        RecruitmentTaskStatus recruitmentTaskStatus,
        LocalDate taskDeadline,
        LocalDate evaluationDeadline,
        RecruitmentProcessStatus status
)
{}
