package com.krzysiek.recruiting.service;


import com.krzysiek.recruiting.dto.responsDTOs.BaseResponseDTO;
import com.krzysiek.recruiting.dto.requestDTOs.RecruitmentProcessRequestDTO;
import com.krzysiek.recruiting.enums.RecruitmentProcessStatus;

import java.util.List;

public interface IRecruitmentProcessService {

    BaseResponseDTO createRecruitmentProcess(RecruitmentProcessRequestDTO recruitmentProcessRequestDTO);
    List<RecruitmentProcessRequestDTO> getAllRecruitmentProcesses(int pageNumber, String sortBy, String sortDirection, RecruitmentProcessStatus status, Long cvId);
//    RecruitmentProcessRequestDTO getSingleRecruitmentProcess(Long recruitmentProcessId);
    void editRecruitmentProcess(Long recruitmentProcessId);
    void deleteRecruitmentProcess(Long recruitmentProcessId);

}
