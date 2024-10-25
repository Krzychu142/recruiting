package com.krzysiek.recruiting.validator.implementation;

import com.krzysiek.recruiting.dto.JobDescriptionDTO;
import com.krzysiek.recruiting.enums.WorkLocation;
import com.krzysiek.recruiting.validator.IJobDescriptionServiceValidator;
import jakarta.validation.ValidationException;
import org.springframework.stereotype.Component;

import java.util.EnumSet;

@Component
public class JobDescriptionServiceValidator implements IJobDescriptionServiceValidator {
    @Override
    public void validateJobDescriptionDTO(JobDescriptionDTO jobDescriptionDTO) {
        if (EnumSet.of(WorkLocation.HYBRID, WorkLocation.ON_SITE).contains(jobDescriptionDTO.workLocation())
                && jobDescriptionDTO.companyAddress() == null) {
            throw new ValidationException("Company address is required for " + WorkLocation.HYBRID + " or " + WorkLocation.ON_SITE + ".");
        }
        if ((jobDescriptionDTO.minRate() != null  && jobDescriptionDTO.maxRate() != null) && jobDescriptionDTO.minRate().compareTo(jobDescriptionDTO.maxRate()) > 0) {
            throw new ValidationException("Min rate must be less than or equal to max rate.");
        }
    }

    @Override
    public void validateEditJobDescriptionDTO(JobDescriptionDTO jobDescriptionDTO) {
        if (jobDescriptionDTO.id() == null) {
            throw new ValidationException("Job description must have an ID to edit.");
        }
        validateJobDescriptionDTO(jobDescriptionDTO);
    }
}
