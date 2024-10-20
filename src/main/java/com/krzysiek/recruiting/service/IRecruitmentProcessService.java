package com.krzysiek.recruiting.service;


import com.krzysiek.recruiting.dto.BaseResponseDTO;
import com.krzysiek.recruiting.dto.CreateRecruitmentProcessRequestDTO;

public interface IRecruitmentProcessService {

    BaseResponseDTO createRecruitmentProcess(CreateRecruitmentProcessRequestDTO createRecruitmentProcessRequestDTO);
    void getAllRecruitmentProcess(int pageNumber);
    void getSingleRecruitmentProcess(Long recruitmentProcessId);
    void editRecruitmentProcess(Long recruitmentProcessId);

}
