package com.krzysiek.recruiting.validator;

import com.krzysiek.recruiting.dto.JobDescriptionDTO;

public interface IJobDescriptionServiceValidator {
    void validateJobDescriptionDTO(JobDescriptionDTO jobDescriptionDTO);
}
