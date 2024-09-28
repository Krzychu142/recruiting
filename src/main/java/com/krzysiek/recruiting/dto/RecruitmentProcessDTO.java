package com.krzysiek.recruiting.dto;

import com.krzysiek.recruiting.model.RecruitmentProcessStatus;
import com.krzysiek.recruiting.model.RecruitmentTaskStatus;
import java.math.BigDecimal;
import java.time.LocalDate;

public record RecruitmentProcessDTO(
        Long id,
        Long userId,
        String companyName,
        LocalDate startDate,
        LocalDate endDate,
        BigDecimal offeredSalaryMin,
        BigDecimal offeredSalaryMax,
        String position,
        String requirements,
        Long cvId,
        Boolean hasRecruitmentTask,
        RecruitmentTaskStatus recruitmentTaskStatus,
        LocalDate taskDeadline,
        LocalDate evaluationDeadline,
        RecruitmentProcessStatus status
) {}
