package com.krzysiek.recruiting.dto;

import com.krzysiek.recruiting.enums.RecruitmentProcessStatus;
import com.krzysiek.recruiting.enums.RecruitmentTaskStatus;
import java.time.LocalDate;

public record RecruitmentProcessDTO(
        Long id,
        Long userId,
        String companyName,
        LocalDate startDate,
        LocalDate endDate,
        Long jobDescriptionId,
        Long cvId,
        Boolean hasRecruitmentTask,
        RecruitmentTaskStatus recruitmentTaskStatus,
        LocalDate taskDeadline,
        RecruitmentProcessStatus status
) {}
