package com.krzysiek.recruiting.service;


import com.krzysiek.recruiting.dto.requestDTOs.RecruitmentProcessRequestDTO;
import com.krzysiek.recruiting.enums.RecruitmentProcessStatus;

import java.util.List;

public interface IRecruitmentProcessService {

    RecruitmentProcessRequestDTO createRecruitmentProcess(RecruitmentProcessRequestDTO recruitmentProcessRequestDTO);
    List<RecruitmentProcessRequestDTO> getAllRecruitmentProcesses(int pageNumber, String sortBy, String sortDirection, RecruitmentProcessStatus status, Long cvId);
    RecruitmentProcessRequestDTO updateRecruitmentProcess(RecruitmentProcessRequestDTO recruitmentProcessRequestDTO);
    void deleteRecruitmentProcess(Long recruitmentProcessId);

}
