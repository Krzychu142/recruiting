package com.krzysiek.recruiting.validator.implementation;

import com.krzysiek.recruiting.dto.requestDTOs.RecruitmentProcessRequestDTO;
import com.krzysiek.recruiting.enums.FileType;
import com.krzysiek.recruiting.service.implementation.FileService;
import com.krzysiek.recruiting.validator.IRecruitmentProcessServiceValidator;
import jakarta.validation.ValidationException;
import org.springframework.stereotype.Component;

@Component
public class RecruitmentProcessServiceValidator implements IRecruitmentProcessServiceValidator {

    private final FileService fileService;

    public RecruitmentProcessServiceValidator(FileService fileService) {
        this.fileService = fileService;
    }

    @Override
    public void validateEditRecruitmentProcessDTO(RecruitmentProcessRequestDTO dto) {

    }

    @Override
    public void validateCreateRecruitmentProcessDTO(RecruitmentProcessRequestDTO dto) {
        if(dto.dateOfApplication() == null) {
            throw new ValidationException("Date of application is required");
        }
        if (!dto.hasRecruitmentTask() && dto.recruitmentTaskId() != null) {
            throw new ValidationException("You can't attach a recruitment task if process is marked as with no recruitment task.");
        }
        if (dto.processEndDate() != null && dto.dateOfApplication().isAfter(dto.processEndDate())) {
            throw new ValidationException("Date of application can't be after process end date.");
        }
        if (dto.taskDeadline() != null && dto.dateOfApplication().isAfter(dto.taskDeadline())) {
            throw new ValidationException("Date of application can't be after task deadline.");
        }
        if (dto.evaluationDeadline() != null && dto.dateOfApplication().isAfter(dto.evaluationDeadline())) {
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
