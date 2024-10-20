package com.krzysiek.recruiting.service;

import com.krzysiek.recruiting.dto.JobDescriptionDTO;
import com.krzysiek.recruiting.enums.WorkLocation;
import com.krzysiek.recruiting.exception.JobDescriptionNotFoundException;
import com.krzysiek.recruiting.exception.ThrowCorrectException;
import com.krzysiek.recruiting.mapper.JobDescriptionMapper;
import com.krzysiek.recruiting.model.JobDescription;
import com.krzysiek.recruiting.repository.JobDescriptionRepository;
import jakarta.validation.ValidationException;
import org.springframework.stereotype.Service;

import java.util.EnumSet;

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
            validateJobDescriptionDTO(jobDescriptionDTO);
            JobDescription jobDescription = jobDescriptionMapper.toEntity(jobDescriptionDTO);
            return jobDescriptionRepository.save(jobDescription);
        } catch (Exception ex) {
            throw throwCorrectException.handleException(ex);
        }
    }

    @Override
    public JobDescription getJobDescriptionById(Long id) {
        try {
            return jobDescriptionRepository.findById(id).orElseThrow(() -> new JobDescriptionNotFoundException("Not found job description with id: " + id));
        } catch (Exception ex) {
            throw throwCorrectException.handleException(ex);
        }
    }

    private void validateJobDescriptionDTO(JobDescriptionDTO jobDescriptionDTO) {
        if (EnumSet.of(WorkLocation.HYBRID, WorkLocation.ON_SITE).contains(jobDescriptionDTO.workLocation())
                && jobDescriptionDTO.companyAddress() == null) {
            throw new ValidationException("Company address is required for " + WorkLocation.HYBRID + " or " + WorkLocation.ON_SITE + ".");
        }
        if ((jobDescriptionDTO.minRate() != null  && jobDescriptionDTO.maxRate() != null) && jobDescriptionDTO.minRate().compareTo(jobDescriptionDTO.maxRate()) > 0) {
            throw new ValidationException("Min rate must be less than or equal to max rate.");
        }
    }
}
