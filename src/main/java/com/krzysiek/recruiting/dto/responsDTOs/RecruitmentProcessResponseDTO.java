package com.krzysiek.recruiting.dto.responsDTOs;

import com.krzysiek.recruiting.dto.requestDTOs.RecruitmentProcessRequestDTO;
import lombok.Getter;

@Getter
public class RecruitmentProcessResponseDTO extends BaseResponseDTO{

    private final RecruitmentProcessRequestDTO recruitmentProcessRequestDTO;

    public RecruitmentProcessResponseDTO(String message, RecruitmentProcessRequestDTO recruitmentProcessRequestDTO) {
        super(message);
        this.recruitmentProcessRequestDTO = recruitmentProcessRequestDTO;
    }
}
