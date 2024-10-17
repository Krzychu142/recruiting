package com.krzysiek.recruiting.service;

import com.krzysiek.recruiting.dto.CreateRecruitmentProcessRequestDTO;
import com.krzysiek.recruiting.enums.FileType;
import com.krzysiek.recruiting.exception.ThrowCorrectException;
import com.krzysiek.recruiting.mapper.RecruitmentProcessMapper;
import com.krzysiek.recruiting.repository.RecruitmentProcessRepository;
import jakarta.validation.ValidationException;
import org.springframework.stereotype.Service;

@Service
public class RecruitmentProcessServiceImplementation implements IRecruitmentProcessService{

    private final RecruitmentProcessRepository recruitmentProcessRepository;
    private final ThrowCorrectException throwCorrectException;
    private final JobDescriptionServiceServiceImplementation jobDescriptionServiceServiceImplementation;
    private final RecruitmentProcessMapper recruitmentProcessMapper;
    private final FileService fileService;

    public RecruitmentProcessServiceImplementation(RecruitmentProcessRepository repository,
                                                   ThrowCorrectException throwCorrectException,
                                                   JobDescriptionServiceServiceImplementation jobDescriptionServiceServiceImplementation,
                                                   RecruitmentProcessMapper recruitmentProcessMapper,
                                                   FileService fileService
    ) {
        this.recruitmentProcessRepository = repository;
        this.throwCorrectException = throwCorrectException;
        this.jobDescriptionServiceServiceImplementation = jobDescriptionServiceServiceImplementation;
        this.recruitmentProcessMapper = recruitmentProcessMapper;
        this.fileService = fileService;
    }

    @Override
    public void createRecruitmentProcess(CreateRecruitmentProcessRequestDTO createRecruitmentProcessRequestDTO) {
        try {
            validateCreateRecruitmentProcessDTO(createRecruitmentProcessRequestDTO);
        } catch (Exception ex) {
            throw throwCorrectException.handleException(ex);
        }
    }

    @Override
    public void getAllRecruitmentProcess(int pageNumber) {

    }

    @Override
    public void getSingleRecruitmentProcess(Long recruitmentProcessId) {

    }

    @Override
    public void editRecruitmentProcess(Long recruitmentProcessId) {

    }

    private void validateCreateRecruitmentProcessDTO(CreateRecruitmentProcessRequestDTO dto) {
        jobDescriptionServiceServiceImplementation.validateJobDescriptionDTO(dto.jobDescriptionDTO());
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
