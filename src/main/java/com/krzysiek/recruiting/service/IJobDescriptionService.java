package com.krzysiek.recruiting.service;

import com.krzysiek.recruiting.dto.JobDescriptionDTO;
import com.krzysiek.recruiting.model.JobDescription;

public interface IJobDescriptionService {
    JobDescription createJobDescription(JobDescriptionDTO jobDescriptionDTO);
    JobDescription getJobDescriptionById(Long id);
    void updateJobDescription(JobDescription oldJobDescription,  JobDescriptionDTO newJobDescriptionDTO);
}
