package com.krzysiek.recruiting;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.krzysiek.recruiting.dto.JobDescriptionDTO;
import com.krzysiek.recruiting.enums.ContractType;
import com.krzysiek.recruiting.enums.WorkLocation;
import com.krzysiek.recruiting.exception.JobDescriptionNotFoundException;
import com.krzysiek.recruiting.exception.ThrowCorrectException;
import com.krzysiek.recruiting.mapper.JobDescriptionMapper;
import com.krzysiek.recruiting.model.JobDescription;
import com.krzysiek.recruiting.repository.JobDescriptionRepository;
import com.krzysiek.recruiting.service.JobDescriptionServiceServiceImplementation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

public class JobDescriptionServiceTests {

    @Mock
    private JobDescriptionRepository jobDescriptionRepository;

    @Mock
    private ThrowCorrectException throwCorrectException;

    @Mock
    private JobDescriptionMapper jobDescriptionMapper;

    @InjectMocks
    private JobDescriptionServiceServiceImplementation jobDescriptionService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createJobDescription_Success() {

        JobDescriptionDTO dto = new JobDescriptionDTO(
                null,
                "Company XYZ",
                "Software Engineer",
                "123 Main St",
                WorkLocation.REMOTE,
                ContractType.B2B_CONTRACT,
                "Develop and maintain software applications.",
                new BigDecimal("100.00"),
                new BigDecimal("200.00")
        );

        JobDescription mappedEntity = new JobDescription();
        mappedEntity.setCompanyName(dto.companyName());
        mappedEntity.setJobTitle(dto.jobTitle());
        mappedEntity.setCompanyAddress(dto.companyAddress());
        mappedEntity.setWorkLocation(dto.workLocation());
        mappedEntity.setContractType(dto.contractType());
        mappedEntity.setRequirements(dto.requirements());
        mappedEntity.setMinRate(dto.minRate());
        mappedEntity.setMaxRate(dto.maxRate());

        JobDescription savedEntity = getJobDescription(dto);

        when(jobDescriptionMapper.toEntity(dto)).thenReturn(mappedEntity);
        when(jobDescriptionRepository.save(mappedEntity)).thenReturn(savedEntity);

        // Act
        JobDescription result = jobDescriptionService.createJobDescription(dto);

        // Assert
        assertNotNull(result, "Saved entity should not be null");
        assertEquals(1L, result.getId(), "ID should be set by repository");
        assertEquals(dto.companyName(), result.getCompanyName(), "Company name should match");
        assertEquals(dto.jobTitle(), result.getJobTitle(), "Job title should match");
        assertEquals(dto.companyAddress(), result.getCompanyAddress(), "Company address should match");
        assertEquals(dto.workLocation(), result.getWorkLocation(), "Work location should match");
        assertEquals(dto.contractType(), result.getContractType(), "Contract type should match");
        assertEquals(dto.requirements(), result.getRequirements(), "Requirements should match");
        assertEquals(dto.minRate(), result.getMinRate(), "MinRate should match");
        assertEquals(dto.maxRate(), result.getMaxRate(), "MaxRate should match");

        verify(jobDescriptionMapper, times(1)).toEntity(dto);

        ArgumentCaptor<JobDescription> jobDescriptionCaptor = ArgumentCaptor.forClass(JobDescription.class);
        verify(jobDescriptionRepository, times(1)).save(jobDescriptionCaptor.capture());

        JobDescription capturedJobDescription = jobDescriptionCaptor.getValue();
        assertEquals(dto.companyName(), capturedJobDescription.getCompanyName(), "Captured Company name should match");
        assertEquals(dto.jobTitle(), capturedJobDescription.getJobTitle(), "Captured Job title should match");
        assertEquals(dto.companyAddress(), capturedJobDescription.getCompanyAddress(), "Captured Company address should match");
        assertEquals(dto.workLocation(), capturedJobDescription.getWorkLocation(), "Captured Work location should match");
        assertEquals(dto.contractType(), capturedJobDescription.getContractType(), "Captured Contract type should match");
        assertEquals(dto.requirements(), capturedJobDescription.getRequirements(), "Captured Requirements should match");
        assertEquals(dto.minRate(), capturedJobDescription.getMinRate(), "Captured MinRate should match");
        assertEquals(dto.maxRate(), capturedJobDescription.getMaxRate(), "Captured MaxRate should match");
    }


    @Test
    void createJobDescription_MapperThrowsException() {
        // Arrange
        JobDescriptionDTO dto = new JobDescriptionDTO(
                null,
                "Company XYZ",
                "Software Engineer",
                "123 Main St",
                WorkLocation.REMOTE,
                ContractType.EMPLOYMENT_CONTRACT,
                "Develop and maintain software applications.",
                new BigDecimal("100.00"),
                new BigDecimal("200.00")
        );

        Exception mapperException = new RuntimeException("Mapper error");

        when(jobDescriptionMapper.toEntity(dto)).thenThrow(mapperException);
        when(throwCorrectException.handleException(mapperException)).thenThrow(new RuntimeException("Handled exception"));

        // Act & Assert
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            jobDescriptionService.createJobDescription(dto);
        });

        assertEquals("Handled exception", thrown.getMessage(), "Exception message should match");

        verify(jobDescriptionMapper, times(1)).toEntity(dto);
        verify(throwCorrectException, times(1)).handleException(mapperException);
        verify(jobDescriptionRepository, never()).save(any(JobDescription.class));
    }

    @Test
    void getJobDescriptionById_Success() {
        // Arrange
        Long id = 1L;
        JobDescription jobDescription = new JobDescription();
        jobDescription.setId(id);
        jobDescription.setCompanyName("Company XYZ");
        jobDescription.setJobTitle("Software Engineer");
        jobDescription.setCompanyAddress("123 Main St");
        jobDescription.setWorkLocation(WorkLocation.REMOTE);
        jobDescription.setContractType(ContractType.B2B_CONTRACT);
        jobDescription.setRequirements("Develop and maintain software applications.");
        jobDescription.setMinRate(new BigDecimal("100.00"));
        jobDescription.setMaxRate(new BigDecimal("200.00"));

        when(jobDescriptionRepository.findById(id)).thenReturn(Optional.of(jobDescription));

        // Act
        JobDescription result = jobDescriptionService.getJobDescriptionById(id);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertEquals(id, result.getId(), "ID should match");
        assertEquals("Company XYZ", result.getCompanyName(), "Company name should match");
        assertEquals("Software Engineer", result.getJobTitle(), "Job title should match");
        assertEquals("123 Main St", result.getCompanyAddress(), "Company address should match");
        assertEquals(WorkLocation.REMOTE, result.getWorkLocation(), "Work location should match");
        assertEquals(ContractType.B2B_CONTRACT, result.getContractType(), "Contract type should match");
        assertEquals("Develop and maintain software applications.", result.getRequirements(), "Requirements should match");
        assertEquals(new BigDecimal("100.00"), result.getMinRate(), "MinRate should match");
        assertEquals(new BigDecimal("200.00"), result.getMaxRate(), "MaxRate should match");

        verify(jobDescriptionRepository, times(1)).findById(id);
        verify(throwCorrectException, never()).handleException(any(Exception.class));
    }

    @Test
    void getJobDescriptionById_NotFound() {
        // Arrange
        Long id = 1L;

        when(jobDescriptionRepository.findById(id)).thenReturn(Optional.empty());
        when(throwCorrectException.handleException(any(JobDescriptionNotFoundException.class)))
                .thenThrow(new RuntimeException("Handled exception"));

        // Act & Assert
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            jobDescriptionService.getJobDescriptionById(id);
        }, "Expected getJobDescriptionById to throw, but it didn't");

        assertEquals("Handled exception", thrown.getMessage(), "Exception message should match");

        verify(jobDescriptionRepository, times(1)).findById(id);
        verify(throwCorrectException, times(1)).handleException(any(JobDescriptionNotFoundException.class));
    }


    private static JobDescription getJobDescription(JobDescriptionDTO dto) {
        JobDescription savedEntity = new JobDescription();
        savedEntity.setId(1L);
        savedEntity.setCompanyName(dto.companyName());
        savedEntity.setJobTitle(dto.jobTitle());
        savedEntity.setCompanyAddress(dto.companyAddress());
        savedEntity.setWorkLocation(dto.workLocation());
        savedEntity.setContractType(dto.contractType());
        savedEntity.setRequirements(dto.requirements());
        savedEntity.setMinRate(dto.minRate());
        savedEntity.setMaxRate(dto.maxRate());
        return savedEntity;
    }

}
