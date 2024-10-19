package com.krzysiek.recruiting.dto;

import com.krzysiek.recruiting.enums.RecruitmentProcessStatus;
import com.krzysiek.recruiting.enums.RecruitmentTaskStatus;
import java.time.LocalDate;

public record RecruitmentProcessDTO(
        Long id,
        Long userId,
        Long jobDescriptionId,
        Long cvId,
        Long recruitmentTask,
        LocalDate dateOfApplication,
        LocalDate processEndDate,
        Boolean hasRecruitmentTask,
        RecruitmentTaskStatus recruitmentTaskStatus,
        LocalDate taskDeadline,
        RecruitmentProcessStatus status
) {}