package com.krzysiek.recruiting.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class AllRecruitmentProcessesResponseDTO extends BaseResponseDTO{

    private final List<RecruitmentProcessRequestDTO> allRecruitmentProcessDTOList;

    public AllRecruitmentProcessesResponseDTO(String message, List<RecruitmentProcessRequestDTO> recruitmentProcessDTOList) {
        super(message);
        this.allRecruitmentProcessDTOList = recruitmentProcessDTOList;
    }
}
