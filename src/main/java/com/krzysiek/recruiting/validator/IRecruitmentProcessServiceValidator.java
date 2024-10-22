package com.krzysiek.recruiting.validator;

import com.krzysiek.recruiting.dto.requestDTOs.RecruitmentProcessRequestDTO;

public interface IRecruitmentProcessServiceValidator {
    void validateCreateRecruitmentProcessDTO(RecruitmentProcessRequestDTO dto);
    void validateEditRecruitmentProcessDTO(RecruitmentProcessRequestDTO dto);
}
