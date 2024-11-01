package com.krzysiek.recruiting.dto.requestDTOs;

import com.krzysiek.recruiting.dto.JobDescriptionDTO;
import com.krzysiek.recruiting.enums.RecruitmentProcessStatus;
import com.krzysiek.recruiting.enums.RecruitmentTaskStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record RecruitmentProcessRequestDTO(
        Long id,
        @Valid
        @NotNull
        JobDescriptionDTO jobDescriptionDTO,
        @NotNull
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
