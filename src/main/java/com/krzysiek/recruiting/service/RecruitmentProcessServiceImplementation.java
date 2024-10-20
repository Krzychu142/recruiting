package com.krzysiek.recruiting.service;

import com.krzysiek.recruiting.dto.BaseResponseDTO;
import com.krzysiek.recruiting.dto.RecruitmentProcessRequestDTO;
import com.krzysiek.recruiting.dto.RecruitmentProcessDTO;
import com.krzysiek.recruiting.enums.FileType;
import com.krzysiek.recruiting.exception.ThrowCorrectException;
import com.krzysiek.recruiting.mapper.RecruitmentProcessMapper;
import com.krzysiek.recruiting.model.JobDescription;
import com.krzysiek.recruiting.model.RecruitmentProcess;
import com.krzysiek.recruiting.repository.RecruitmentProcessRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecruitmentProcessServiceImplementation implements IRecruitmentProcessService{

    private final RecruitmentProcessRepository recruitmentProcessRepository;
    private final ThrowCorrectException throwCorrectException;
    private final JobDescriptionServiceServiceImplementation jobDescriptionServiceService;
    private final RecruitmentProcessMapper recruitmentProcessMapper;
    private final FileService fileService;
    private final AuthenticationService authenticationService;

    public RecruitmentProcessServiceImplementation(RecruitmentProcessRepository repository,
                                                   ThrowCorrectException throwCorrectException,
                                                   JobDescriptionServiceServiceImplementation jobDescriptionServiceServiceImplementation,
                                                   RecruitmentProcessMapper recruitmentProcessMapper,
                                                   FileService fileService,
                                                   AuthenticationService authenticationService) {
        this.recruitmentProcessRepository = repository;
        this.throwCorrectException = throwCorrectException;
        this.jobDescriptionServiceService = jobDescriptionServiceServiceImplementation;
        this.recruitmentProcessMapper = recruitmentProcessMapper;
        this.fileService = fileService;
        this.authenticationService = authenticationService;
    }

    @Transactional
    @Override
    public BaseResponseDTO createRecruitmentProcess(RecruitmentProcessRequestDTO recruitmentProcessRequestDTO) {
        try {
            validateCreateRecruitmentProcessDTO(recruitmentProcessRequestDTO);
            JobDescription jobDescription = jobDescriptionServiceService.createJobDescription(recruitmentProcessRequestDTO.jobDescriptionDTO());
            RecruitmentProcessDTO recruitmentProcessDTO = new RecruitmentProcessDTO(
                    null,
                    authenticationService.getLoggedInUserId(),
                    jobDescription.getId(),
                    recruitmentProcessRequestDTO.cvId(),
                    recruitmentProcessRequestDTO.recruitmentTaskId(),
                    recruitmentProcessRequestDTO.dateOfApplication(),
                    recruitmentProcessRequestDTO.processEndDate(),
                    recruitmentProcessRequestDTO.hasRecruitmentTask(),
                    recruitmentProcessRequestDTO.recruitmentTaskStatus(),
                    recruitmentProcessRequestDTO.taskDeadline(),
                    recruitmentProcessRequestDTO.status()
            );
            RecruitmentProcess recruitmentProcess = recruitmentProcessMapper.toEntity(recruitmentProcessDTO);
            recruitmentProcessRepository.save(recruitmentProcess);
            return new BaseResponseDTO("Successfully created recruitment process");
        } catch (Exception ex) {
            throw throwCorrectException.handleException(ex);
        }
    }

    @Override
    public List<RecruitmentProcessRequestDTO> getAllRecruitmentProcesses(int pageNumber) {
        try {
            Pageable pageable = PageRequest.of(pageNumber, 5);
            Page<RecruitmentProcess> usersAllRecruitmentProcess = recruitmentProcessRepository.findAllByUserId(authenticationService.getLoggedInUserId(), pageable);
            return usersAllRecruitmentProcess.stream()
                    .map(recruitmentProcessMapper::toRecruitmentProcessDTO)
                    .collect(Collectors.toList());
        } catch (Exception ex) {
            throw throwCorrectException.handleException(ex);
        }
    }

    @Override
    public void getSingleRecruitmentProcess(Long recruitmentProcessId) {

    }

    @Override
    public void editRecruitmentProcess(Long recruitmentProcessId) {

    }

    private void validateCreateRecruitmentProcessDTO(RecruitmentProcessRequestDTO dto) {
        if (!dto.hasRecruitmentTask() && dto.recruitmentTaskId() != null) {
            throw new ValidationException("You can't attach a recruitment task if process is marked as with no recruitment task.");
        }
        if (dto.dateOfApplication() != null && dto.processEndDate() != null && dto.dateOfApplication().isAfter(dto.processEndDate())) {
            throw new ValidationException("Date of application can't be after process end date.");
        }
        if (dto.dateOfApplication() != null && dto.taskDeadline() != null && dto.dateOfApplication().isAfter(dto.taskDeadline())) {
            throw new ValidationException("Date of application can't be after task deadline.");
        }
        if (dto.dateOfApplication() != null && dto.evaluationDeadline() != null && dto.dateOfApplication().isAfter(dto.evaluationDeadline())) {
            throw new ValidationException("Date of application can't be after evaluation deadline.");
        }
        if (dto.taskDeadline() != null && dto.evaluationDeadline() != null && dto.taskDeadline().isAfter(dto.evaluationDeadline())) {
            throw new ValidationException("Task deadline can't be after evaluation deadline.");
        }
        if (!dto.hasRecruitmentTask() && dto.recruitmentTaskStatus() != null) {
            throw new ValidationException("Invalid task status for process with attached recruitment task.");
        }
        if (dto.hasRecruitmentTask() && dto.recruitmentTaskStatus() == null) {
            throw new ValidationException("Recruitment task status is required if there is a recruitment task.");
        }
        if (dto.cvId() != null) {
            fileService.checkIsFileExistsInDatabaseByIdTypeUserId(dto.cvId(), FileType.CV);
        }
        if (dto.recruitmentTaskId() != null) {
            fileService.checkIsFileExistsInDatabaseByIdTypeUserId(dto.recruitmentTaskId(), FileType.RECRUITMENT_TASK);
        }
    }
}
