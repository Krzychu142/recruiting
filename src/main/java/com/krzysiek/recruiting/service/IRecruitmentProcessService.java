package com.krzysiek.recruiting.service;


import com.krzysiek.recruiting.dto.BaseResponseDTO;
import com.krzysiek.recruiting.dto.RecruitmentProcessRequestDTO;

import java.util.List;

public interface IRecruitmentProcessService {

    BaseResponseDTO createRecruitmentProcess(RecruitmentProcessRequestDTO recruitmentProcessRequestDTO);
    List<RecruitmentProcessRequestDTO> getAllRecruitmentProcesses(int pageNumber);
    void getSingleRecruitmentProcess(Long recruitmentProcessId);
    void editRecruitmentProcess(Long recruitmentProcessId);

}
