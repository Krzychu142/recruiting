package com.krzysiek.recruiting.service;

import com.krzysiek.recruiting.dto.CreateRecruitmentProcessRequestDTO;
import com.krzysiek.recruiting.dto.JobDescriptionDTO;
import com.krzysiek.recruiting.exception.ThrowCorrectException;
import com.krzysiek.recruiting.mapper.RecruitmentProcessMapper;
import com.krzysiek.recruiting.repository.RecruitmentProcessRepository;
import org.springframework.stereotype.Service;

@Service
public class RecruitmentProcessServiceImplementation implements IRecruitmentProcessService{

    private final RecruitmentProcessRepository recruitmentProcessRepository;
    private final ThrowCorrectException throwCorrectException;
    private final JobDescriptionServiceServiceImplementation jobDescriptionServiceServiceImplementation;
    private final RecruitmentProcessMapper recruitmentProcessMapper;

    public RecruitmentProcessServiceImplementation(RecruitmentProcessRepository repository, ThrowCorrectException throwCorrectException, JobDescriptionServiceServiceImplementation jobDescriptionServiceServiceImplementation, RecruitmentProcessMapper recruitmentProcessMapper) {
        this.recruitmentProcessRepository = repository;
        this.throwCorrectException = throwCorrectException;
        this.jobDescriptionServiceServiceImplementation = jobDescriptionServiceServiceImplementation;
        this.recruitmentProcessMapper = recruitmentProcessMapper;
    }

    @Override
    public void createRecruitmentProcess(CreateRecruitmentProcessRequestDTO createRecruitmentProcessRequestDTO) {
        try {
            //implementation
            validateCreateRecruitmentProcessDTO(createRecruitmentProcessRequestDTO);
        } catch (Exception ex) {
            throw throwCorrectException.handleException(ex);
        }
    }

    @Override
    public void getAllRecruitmentProcess(int pageNumber) {

    }

    @Override
    public void getSingleRecruitmentProcess(Long recruitmentProcessId) {

    }

    @Override
    public void editRecruitmentProcess(Long recruitmentProcessId) {

    }

    private void validateCreateRecruitmentProcessDTO(CreateRecruitmentProcessRequestDTO dto) {
        jobDescriptionServiceServiceImplementation.validateJobDescriptionDTO(dto.jobDescriptionDTO());
    }
}
