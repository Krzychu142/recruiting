package com.krzysiek.recruiting.service;

import com.krzysiek.recruiting.dto.JobDescriptionDTO;
import com.krzysiek.recruiting.exception.ThrowCorrectException;
import com.krzysiek.recruiting.mapper.JobDescriptionMapper;
import com.krzysiek.recruiting.model.JobDescription;
import com.krzysiek.recruiting.repository.JobDescriptionRepository;
import org.springframework.stereotype.Service;

@Service
public class JobDescriptionServiceServiceImplementation implements IJobDescriptionService {

    private final ThrowCorrectException throwCorrectException;
    private final JobDescriptionRepository jobDescriptionRepository;
    private final JobDescriptionMapper jobDescriptionMapper;

    public JobDescriptionServiceServiceImplementation (ThrowCorrectException throwCorrectException, JobDescriptionRepository jobDescriptionRepository, JobDescriptionMapper jobDescriptionMapper) {
        this.throwCorrectException = throwCorrectException;
        this.jobDescriptionRepository = jobDescriptionRepository;
        this.jobDescriptionMapper = jobDescriptionMapper;
    }

    @Override
    public JobDescription createJobDescription(JobDescriptionDTO jobDescriptionDTO) {
        try {
            JobDescription jobDescription = jobDescriptionMapper.toEntity(jobDescriptionDTO);
            return jobDescriptionRepository.save(jobDescription);
        } catch (Exception ex) {
            throw throwCorrectException.handleException(ex);
        }
    }
}
