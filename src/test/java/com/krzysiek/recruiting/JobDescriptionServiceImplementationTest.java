package com.krzysiek.recruiting;

import com.krzysiek.recruiting.dto.JobDescriptionDTO;
import com.krzysiek.recruiting.enums.ContractType;
import com.krzysiek.recruiting.enums.WorkLocation;
import com.krzysiek.recruiting.exception.ThrowCorrectException;
import com.krzysiek.recruiting.exception.customExceptions.JobDescriptionAlreadyExistsException;
import com.krzysiek.recruiting.exception.customExceptions.JobDescriptionNotFoundException;
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
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
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

    @Test
    void testGetJobDescriptionById_NotFound(){
        // Given
        Long searchedId = 2L;
        String exceptionMessage = "Not found job description";
        when(jobDescriptionRepository.findById(searchedId)).thenReturn(Optional.empty());
        JobDescriptionNotFoundException exception = new JobDescriptionNotFoundException(exceptionMessage);
        when(throwCorrectException.handleException(any(JobDescriptionNotFoundException.class))).thenReturn(exception);

        // When
        assertThatThrownBy(() -> serviceImplementation.getJobDescriptionById(searchedId))
                .isInstanceOf(JobDescriptionNotFoundException.class)
                .hasMessage(exceptionMessage);
        // Then
        verify(jobDescriptionRepository, times(1)).findById(searchedId);
        verify(throwCorrectException, times(1)).handleException(any(JobDescriptionNotFoundException.class));
    }

    @Test
    void testCreateJobDescription_AlreadyExists(){
        // Given
        String companyName = "test company name";
        String jobTitle = "test title";
        String requirements = "test requirements";
        String exceptionMessage = "Job already exists.";


        JobDescriptionDTO jobDescriptionDTO = new JobDescriptionDTO(
                null,
                companyName,
                jobTitle,
                null,
                null,
                null,
                requirements,
                null,
                null
        );

        when(jobDescriptionRepository.existsByCompanyNameAndJobTitleAndRequirements(companyName, jobTitle, requirements)).thenReturn(true);
        JobDescriptionAlreadyExistsException exception = new JobDescriptionAlreadyExistsException(exceptionMessage);
        when(throwCorrectException.handleException(any(JobDescriptionAlreadyExistsException.class))).thenReturn(exception);

        // When
        assertThatThrownBy(() -> serviceImplementation.createJobDescription(jobDescriptionDTO))
                .isInstanceOf(JobDescriptionAlreadyExistsException.class)
                .hasMessage(exceptionMessage);

        // Then
        verify(jobDescriptionRepository, times(1)).existsByCompanyNameAndJobTitleAndRequirements(companyName, jobTitle, requirements);
        verify(throwCorrectException, times(1)).handleException(any(JobDescriptionAlreadyExistsException.class));

    }
}
