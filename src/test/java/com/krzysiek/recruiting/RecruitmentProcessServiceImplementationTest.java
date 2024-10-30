package com.krzysiek.recruiting;

import com.krzysiek.recruiting.dto.JobDescriptionDTO;
import com.krzysiek.recruiting.dto.RecruitmentProcessDTO;
import com.krzysiek.recruiting.dto.requestDTOs.RecruitmentProcessRequestDTO;
import com.krzysiek.recruiting.enums.ContractType;
import com.krzysiek.recruiting.enums.RecruitmentProcessStatus;
import com.krzysiek.recruiting.enums.RecruitmentTaskStatus;
import com.krzysiek.recruiting.enums.WorkLocation;
import com.krzysiek.recruiting.exception.ThrowCorrectException;
import com.krzysiek.recruiting.mapper.RecruitmentProcessMapper;
import com.krzysiek.recruiting.model.JobDescription;
import com.krzysiek.recruiting.model.RecruitmentProcess;
import com.krzysiek.recruiting.repository.RecruitmentProcessRepository;
import com.krzysiek.recruiting.service.IJobDescriptionService;
import com.krzysiek.recruiting.service.implementation.AuthenticationService;
import com.krzysiek.recruiting.service.implementation.RecruitmentProcessServiceImplementation;
import com.krzysiek.recruiting.validator.IRecruitmentProcessServiceValidator;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


import java.math.BigDecimal;
import java.time.LocalDate;


@ExtendWith(MockitoExtension.class)
public class RecruitmentProcessServiceImplementationTest {
    @Mock
    private RecruitmentProcessRepository recruitmentProcessRepository;

    @Mock
    private ThrowCorrectException throwCorrectException;

    @Mock
    private IJobDescriptionService jobDescriptionService;

    @Mock
    private RecruitmentProcessMapper recruitmentProcessMapper;

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private IRecruitmentProcessServiceValidator recruitmentProcessServiceValidator;

    @InjectMocks
    private RecruitmentProcessServiceImplementation recruitmentProcessService;

    @Test
    void testCreateRecruitmentProcess_Success() {
        // Given
        RecruitmentProcessRequestDTO requestDTO = new RecruitmentProcessRequestDTO(
                null,
                new JobDescriptionDTO(
                        null,
                        "TEST NAME",
                        "TEST JOB TITLE",
                        "KRAKÓW JANA PAWŁA 39",
                        WorkLocation.ON_SITE,
                        ContractType.EMPLOYMENT_CONTRACT,
                        "JAVA, SPRING, GIT",
                        BigDecimal.valueOf(8000.00),
                        BigDecimal.valueOf(12000.00)
                ),
                1L,
                2L,
                LocalDate.parse("2023-10-01"),
                LocalDate.parse("2023-12-01"),
                true,
                RecruitmentTaskStatus.PENDING,
                LocalDate.parse("2023-11-01"),
                LocalDate.parse("2023-12-01"),
                RecruitmentProcessStatus.IN_PROGRESS
        );

        JobDescription createdJobDescription = new JobDescription();
        createdJobDescription.setId(1L);
        createdJobDescription.setCompanyName("TEST NAME");
        createdJobDescription.setJobTitle("TEST JOB TITLE");
        createdJobDescription.setContractType(ContractType.EMPLOYMENT_CONTRACT);
        createdJobDescription.setRequirements("JAVA, SPRING, GIT");
        createdJobDescription.setMinRate(BigDecimal.valueOf(8000.00));
        createdJobDescription.setMaxRate(BigDecimal.valueOf(12000.00));

        RecruitmentProcess savedRecruitmentProcess = new RecruitmentProcess();
        savedRecruitmentProcess.setId(10L);
        // other fields no needed

        RecruitmentProcessRequestDTO savedRecruitmentProcessRequestDTO = new RecruitmentProcessRequestDTO(
                10L,
                new JobDescriptionDTO(
                        1L,
                        "TEST NAME",
                        "TEST JOB TITLE",
                        "KRAKÓW JANA PAWŁA 39",
                        WorkLocation.ON_SITE,
                        ContractType.EMPLOYMENT_CONTRACT,
                        "JAVA, SPRING, GIT",
                        BigDecimal.valueOf(8000.00),
                        BigDecimal.valueOf(12000.00)
                ),
                1L,
                2L,
                LocalDate.parse("2023-10-01"),
                LocalDate.parse("2023-12-01"),
                true,
                RecruitmentTaskStatus.PENDING,
                LocalDate.parse("2023-11-01"),
                LocalDate.parse("2023-12-01"),
                RecruitmentProcessStatus.IN_PROGRESS
        );

        doNothing().when(recruitmentProcessServiceValidator).validateCreateRecruitmentProcessDTO(requestDTO);
        when(authenticationService.getLoggedInUserId()).thenReturn(100L);
        when(jobDescriptionService.createJobDescription(any(JobDescriptionDTO.class))).thenReturn(createdJobDescription);
        when(recruitmentProcessMapper.toEntity(any(RecruitmentProcessDTO.class))).thenReturn(new RecruitmentProcess());
        when(recruitmentProcessRepository.save(any(RecruitmentProcess.class))).thenReturn(savedRecruitmentProcess);
        when(recruitmentProcessMapper.toRecruitmentProcessDTO(any(RecruitmentProcess.class))).thenReturn(savedRecruitmentProcessRequestDTO);

        // When
        RecruitmentProcessRequestDTO result = recruitmentProcessService.createRecruitmentProcess(requestDTO);

        // Then
        assertNotNull(result);
        assertEquals(10L, result.id());
        assertEquals(RecruitmentProcessStatus.IN_PROGRESS, result.status());

        // Verify interactions
        verify(recruitmentProcessServiceValidator, times(1)).validateCreateRecruitmentProcessDTO(requestDTO);
        verify(authenticationService, times(1)).getLoggedInUserId();
        verify(jobDescriptionService, times(1)).createJobDescription(any(JobDescriptionDTO.class));
        verify(recruitmentProcessMapper, times(1)).toEntity(any(RecruitmentProcessDTO.class));
        verify(recruitmentProcessRepository, times(1)).save(any(RecruitmentProcess.class));
        verify(recruitmentProcessMapper, times(1)).toRecruitmentProcessDTO(any(RecruitmentProcess.class));
    }


    @Test
    void testCreateRecruitmentProcess_Fail_EndDateBeforeApplicationDate() {
        // Given
        RecruitmentProcessRequestDTO requestDTO = new RecruitmentProcessRequestDTO(
                null,
                new JobDescriptionDTO(
                        null,
                        "TEST NAME",
                        "TEST JOB TITLE",
                        "KRAKÓW JANA PAWŁA 39",
                        WorkLocation.ON_SITE,
                        ContractType.EMPLOYMENT_CONTRACT,
                        "JAVA, SPRING, GIT",
                        BigDecimal.valueOf(8000.00),
                        BigDecimal.valueOf(12000.00)
                ),
                1L,
                2L,
                LocalDate.parse("2023-12-01"), // dateOfApplication
                LocalDate.parse("2023-10-01"), // processEndDate < dateOfApplication
                true,
                RecruitmentTaskStatus.PENDING,
                LocalDate.parse("2023-11-01"),
                LocalDate.parse("2023-12-01"),
                RecruitmentProcessStatus.IN_PROGRESS
        );

        doThrow(new ValidationException("Process end date cannot be before application date."))
                .when(recruitmentProcessServiceValidator).validateCreateRecruitmentProcessDTO(requestDTO);

        when(throwCorrectException.handleException(any(Exception.class)))
                .thenReturn(new ValidationException("Process end date cannot be before application date."));

        // When & Then
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> recruitmentProcessService.createRecruitmentProcess(requestDTO)
        );

        assertEquals("Process end date cannot be before application date.", exception.getMessage());

        // Verify interactions
        verify(recruitmentProcessServiceValidator, times(1)).validateCreateRecruitmentProcessDTO(requestDTO);
        verify(authenticationService, never()).getLoggedInUserId();
        verify(jobDescriptionService, never()).createJobDescription(any(JobDescriptionDTO.class));
        verify(recruitmentProcessMapper, never()).toEntity(any(RecruitmentProcessDTO.class));
        verify(recruitmentProcessRepository, never()).save(any(RecruitmentProcess.class));
        verify(recruitmentProcessMapper, never()).toRecruitmentProcessDTO(any(RecruitmentProcess.class));
        verify(throwCorrectException, times(1)).handleException(any(Exception.class));
    }

}
