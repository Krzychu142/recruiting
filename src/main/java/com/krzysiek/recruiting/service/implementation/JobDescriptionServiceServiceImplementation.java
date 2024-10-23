package com.krzysiek.recruiting.service.implementation;

import com.krzysiek.recruiting.dto.JobDescriptionDTO;
import com.krzysiek.recruiting.exception.customExceptions.JobDescriptionAlreadyExistsException;
import com.krzysiek.recruiting.exception.customExceptions.JobDescriptionNotFoundException;
import com.krzysiek.recruiting.exception.ThrowCorrectException;
import com.krzysiek.recruiting.mapper.JobDescriptionMapper;
import com.krzysiek.recruiting.model.JobDescription;
import com.krzysiek.recruiting.repository.JobDescriptionRepository;
import com.krzysiek.recruiting.service.IJobDescriptionService;
import com.krzysiek.recruiting.validator.IJobDescriptionServiceValidator;
import org.springframework.stereotype.Service;

@Service
public class JobDescriptionServiceServiceImplementation implements IJobDescriptionService {

    private final ThrowCorrectException throwCorrectException;
    private final JobDescriptionRepository jobDescriptionRepository;
    private final JobDescriptionMapper jobDescriptionMapper;
    private final IJobDescriptionServiceValidator jobDescriptionValidator;

    public JobDescriptionServiceServiceImplementation (ThrowCorrectException throwCorrectException, JobDescriptionRepository jobDescriptionRepository, JobDescriptionMapper jobDescriptionMapper, IJobDescriptionServiceValidator jobDescriptionValidator) {
        this.throwCorrectException = throwCorrectException;
        this.jobDescriptionRepository = jobDescriptionRepository;
        this.jobDescriptionMapper = jobDescriptionMapper;
        this.jobDescriptionValidator = jobDescriptionValidator;
    }

    @Override
    public JobDescription createJobDescription(JobDescriptionDTO jobDescriptionDTO) {
        try {
            jobDescriptionValidator.validateJobDescriptionDTO(jobDescriptionDTO);
            if (isJobDescriptionExists(jobDescriptionDTO)) {
                throw new JobDescriptionAlreadyExistsException("Job description with company name: " + jobDescriptionDTO.companyName() + ", job title: " + jobDescriptionDTO.jobTitle() + " and requirements: " + jobDescriptionDTO.requirements() + " already exists");
            }
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

    @Override
    public void updateJobDescription(JobDescription oldJobDescription, JobDescriptionDTO newJobDescriptionDTO) {
        oldJobDescription.setCompanyName(newJobDescriptionDTO.companyName());
        oldJobDescription.setJobTitle(newJobDescriptionDTO.jobTitle());
        oldJobDescription.setCompanyAddress(newJobDescriptionDTO.companyAddress());
        oldJobDescription.setWorkLocation(newJobDescriptionDTO.workLocation());
        oldJobDescription.setContractType(newJobDescriptionDTO.contractType());
        oldJobDescription.setRequirements(newJobDescriptionDTO.requirements());
        oldJobDescription.setMinRate(newJobDescriptionDTO.minRate());
        oldJobDescription.setMaxRate(newJobDescriptionDTO.maxRate());
        jobDescriptionRepository.save(oldJobDescription);
    }

    private boolean isJobDescriptionExists(JobDescriptionDTO jobDescriptionDTO) {
        return jobDescriptionRepository.existsByCompanyNameAndJobTitleAndRequirements(jobDescriptionDTO.companyName(), jobDescriptionDTO.jobTitle(), jobDescriptionDTO.requirements());
    }
}
