package com.krzysiek.recruiting;

import com.krzysiek.recruiting.dto.JobDescriptionDTO;
import com.krzysiek.recruiting.enums.ContractType;
import com.krzysiek.recruiting.enums.WorkLocation;
import com.krzysiek.recruiting.exception.ThrowCorrectException;
import com.krzysiek.recruiting.mapper.JobDescriptionMapper;
import com.krzysiek.recruiting.model.JobDescription;
import com.krzysiek.recruiting.repository.JobDescriptionRepository;
import com.krzysiek.recruiting.service.implementation.JobDescriptionServiceImplementation;
import com.krzysiek.recruiting.validator.IJobDescriptionServiceValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JobDescriptionServiceImplementationTest {

    @Mock
    private ThrowCorrectException throwCorrectException;

    @Mock
    private JobDescriptionRepository jobDescriptionRepository;

    @Mock
    private JobDescriptionMapper jobDescriptionMapper;

    @Mock
    private IJobDescriptionServiceValidator jobDescriptionServiceValidator;

    @InjectMocks
    private JobDescriptionServiceImplementation serviceImplementation;

    @Test
    void testCreateJobDescription_Success(){
        // Given

        Long id = 1L;
        String companyName = "test company name";
        String jobTitle = "test title";
        String  companyAddress = "test address";
        WorkLocation workLocation = WorkLocation.ON_SITE;
        ContractType contractType = ContractType.EMPLOYMENT_CONTRACT;
        String requirements = "test requirements";
        BigDecimal minRate = BigDecimal.valueOf(8000.00);
        BigDecimal maxRate = BigDecimal.valueOf(12000.00);

        JobDescriptionDTO givenJobDescriptionDTO = new JobDescriptionDTO(
          null, companyName, jobTitle, companyAddress, workLocation, contractType, requirements, minRate, maxRate
        );

        JobDescription saveJobDescription = new JobDescription();
        saveJobDescription.setId(id);
        saveJobDescription.setCompanyName(companyName);
        saveJobDescription.setJobTitle(jobTitle);
        saveJobDescription.setCompanyAddress(companyAddress);
        saveJobDescription.setWorkLocation(workLocation);
        saveJobDescription.setContractType(contractType);
        saveJobDescription.setRequirements(requirements);
        saveJobDescription.setMinRate(minRate);
        saveJobDescription.setMaxRate(maxRate);

        when(jobDescriptionMapper.toEntity(givenJobDescriptionDTO)).thenReturn(saveJobDescription);
        when(jobDescriptionRepository.save(saveJobDescription)).thenReturn(saveJobDescription);

        // When
        JobDescription result = serviceImplementation.createJobDescription(givenJobDescriptionDTO);

        // Then
        assertNotNull(result);
        verify(jobDescriptionServiceValidator, times(1)).validateJobDescriptionDTO(givenJobDescriptionDTO);
        assertEquals(id, result.getId());
        assertEquals(companyName, result.getCompanyName());
        assertEquals(jobTitle, result.getJobTitle());
        assertEquals(companyAddress, result.getCompanyAddress());
        assertEquals(workLocation, result.getWorkLocation());
        assertEquals(contractType, result.getContractType());
        assertEquals(requirements, result.getRequirements());
        assertEquals(minRate, result.getMinRate());
        assertEquals(maxRate, result.getMaxRate());

    }
}
