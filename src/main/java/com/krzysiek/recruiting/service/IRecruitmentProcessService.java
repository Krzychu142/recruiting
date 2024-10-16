package com.krzysiek.recruiting.service;


public interface IRecruitmentProcessService {

    void createRecruitmentProcess();
    void getAllRecruitmentProcess(int pageNumber);
    void getSingleRecruitmentProcess(Long recruitmentProcessId);
    void editRecruitmentProcess(Long recruitmentProcessId);

}
