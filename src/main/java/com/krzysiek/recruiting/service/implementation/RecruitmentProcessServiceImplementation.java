package com.krzysiek.recruiting.service.implementation;

import com.krzysiek.recruiting.dto.JobDescriptionDTO;
import com.krzysiek.recruiting.dto.responsDTOs.BaseResponseDTO;
import com.krzysiek.recruiting.dto.requestDTOs.RecruitmentProcessRequestDTO;
import com.krzysiek.recruiting.dto.RecruitmentProcessDTO;
import com.krzysiek.recruiting.enums.RecruitmentProcessStatus;
import com.krzysiek.recruiting.exception.ThrowCorrectException;
import com.krzysiek.recruiting.exception.customExceptions.RecruitmentProcessNotFoundException;
import com.krzysiek.recruiting.mapper.RecruitmentProcessMapper;
import com.krzysiek.recruiting.model.File;
import com.krzysiek.recruiting.model.JobDescription;
import com.krzysiek.recruiting.model.RecruitmentProcess;
import com.krzysiek.recruiting.repository.RecruitmentProcessRepository;
import com.krzysiek.recruiting.repository.specyfication.RecruitmentProcessSpecification;
import com.krzysiek.recruiting.service.IJobDescriptionService;
import com.krzysiek.recruiting.service.IRecruitmentProcessService;
import com.krzysiek.recruiting.validator.IJobDescriptionServiceValidator;
import com.krzysiek.recruiting.validator.IRecruitmentProcessServiceValidator;
import com.krzysiek.recruiting.validator.implementation.RecruitmentProcessSortValidator;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class RecruitmentProcessServiceImplementation implements IRecruitmentProcessService {

    private final RecruitmentProcessRepository recruitmentProcessRepository;
    private final ThrowCorrectException throwCorrectException;
    private final IJobDescriptionService jobDescriptionService;
    private final RecruitmentProcessMapper recruitmentProcessMapper;
    private final AuthenticationService authenticationService;
    private final RecruitmentProcessSortValidator sortValidator;
    private final IRecruitmentProcessServiceValidator recruitmentProcessServiceValidator;
    private final IJobDescriptionServiceValidator jobDescriptionServiceValidator;
    private final FileService fileService;

    public RecruitmentProcessServiceImplementation(RecruitmentProcessRepository repository,
                                                   ThrowCorrectException throwCorrectException,
                                                   IJobDescriptionService jobDescriptionService,
                                                   RecruitmentProcessMapper recruitmentProcessMapper,
                                                   AuthenticationService authenticationService,
                                                   RecruitmentProcessSortValidator sortValidator,
                                                   IRecruitmentProcessServiceValidator recruitmentProcessServiceValidator,
                                                   IJobDescriptionServiceValidator jobDescriptionServiceValidator,
                                                   FileService fileService) {
        this.recruitmentProcessRepository = repository;
        this.throwCorrectException = throwCorrectException;
        this.jobDescriptionService = jobDescriptionService;
        this.recruitmentProcessMapper = recruitmentProcessMapper;
        this.authenticationService = authenticationService;
        this.sortValidator = sortValidator;
        this.recruitmentProcessServiceValidator = recruitmentProcessServiceValidator;
        this.jobDescriptionServiceValidator = jobDescriptionServiceValidator;
        this.fileService = fileService;
    }

    @Transactional
    @Override
    public RecruitmentProcessRequestDTO createRecruitmentProcess(RecruitmentProcessRequestDTO recruitmentProcessRequestDTO) {
        try {
            recruitmentProcessServiceValidator.validateCreateRecruitmentProcessDTO(recruitmentProcessRequestDTO);
            JobDescription jobDescription = jobDescriptionService.createJobDescription(recruitmentProcessRequestDTO.jobDescriptionDTO());
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
            return recruitmentProcessMapper.toRecruitmentProcessDTO(recruitmentProcessRepository.save(recruitmentProcess));
        } catch (Exception ex) {
            throw throwCorrectException.handleException(ex);
        }
    }

    @Override
    public List<RecruitmentProcessRequestDTO> getAllRecruitmentProcesses(int pageNumber, String sortBy, String sortDirection, RecruitmentProcessStatus status, Long cvId) {
        try {
            Sort.Direction direction = sortValidator.validateSortDirection(sortDirection);
            sortValidator.validateSortBy(sortBy);
            Pageable pageable = PageRequest.of(pageNumber, 5, Sort.by(direction, sortBy));
            Long userId = authenticationService.getLoggedInUserId();
            Specification<RecruitmentProcess> specification = RecruitmentProcessSpecification.buildSpecification(userId, status, cvId);
            Page<RecruitmentProcess> recruitmentProcesses = recruitmentProcessRepository.findAll(specification, pageable);

            return recruitmentProcesses.stream()
                    .map(recruitmentProcessMapper::toRecruitmentProcessDTO)
                    .collect(Collectors.toList());
        } catch (Exception ex) {
            throw throwCorrectException.handleException(ex);
        }
    }

    @Transactional
    @Override
    public RecruitmentProcessRequestDTO updateRecruitmentProcess(RecruitmentProcessRequestDTO recruitmentProcessRequestDTO) {
        try {
            recruitmentProcessServiceValidator.validateEditRecruitmentProcessDTO(recruitmentProcessRequestDTO);
            JobDescriptionDTO newJobDescriptionDTO = recruitmentProcessRequestDTO.jobDescriptionDTO();
            jobDescriptionServiceValidator.validateEditJobDescriptionDTO(newJobDescriptionDTO);
            RecruitmentProcess oldRecruitmentProcess = getSingleRecruitmentProcessDTO(recruitmentProcessRequestDTO.id());
            JobDescription oldJobDescription = oldRecruitmentProcess.getJobDescription();
            if (!Objects.equals(oldRecruitmentProcess.getJobDescription().getId(), newJobDescriptionDTO.id())) {
                throw new ValidationException("Assigned to old recruitment process id of job description is different than provided new.");
            }
            jobDescriptionService.updateJobDescription(oldJobDescription, newJobDescriptionDTO);
            //TODO: segment with files is to optimization, but as for now, let it be as it is
            Long newCvId = recruitmentProcessRequestDTO.cvId();
            if (!oldRecruitmentProcess.getCv().getId().equals(newCvId)) {
                File newCv = fileService.getFileById(newCvId);
                oldRecruitmentProcess.setCv(newCv);
            }
            Long newRecruitmentTaskId = recruitmentProcessRequestDTO.recruitmentTaskId();
            File oldRecruitingTask = oldRecruitmentProcess.getRecruitmentTask();
            if (oldRecruitingTask != null && !Objects.equals(oldRecruitingTask.getId(), newRecruitmentTaskId)) {
                File newRecruitingTask = fileService.getFileById(newRecruitmentTaskId);
                oldRecruitmentProcess.setRecruitmentTask(newRecruitingTask);
            }
            oldRecruitmentProcess.setDateOfApplication(recruitmentProcessRequestDTO.dateOfApplication());
            oldRecruitmentProcess.setProcessEndDate(recruitmentProcessRequestDTO.processEndDate());
            oldRecruitmentProcess.setHasRecruitmentTask(recruitmentProcessRequestDTO.hasRecruitmentTask());
            oldRecruitmentProcess.setRecruitmentTaskStatus(recruitmentProcessRequestDTO.recruitmentTaskStatus());
            oldRecruitmentProcess.setTaskDeadline(recruitmentProcessRequestDTO.taskDeadline());
            oldRecruitmentProcess.setEvaluationDeadline(recruitmentProcessRequestDTO.evaluationDeadline());
            oldRecruitmentProcess.setStatus(recruitmentProcessRequestDTO.status());
            return recruitmentProcessMapper.toRecruitmentProcessDTO(recruitmentProcessRepository.save(oldRecruitmentProcess));
        } catch (Exception ex) {
            throw throwCorrectException.handleException(ex);
        }
    }

    @Override
    public void deleteRecruitmentProcess(Long recruitmentProcessId) {

    }

    private RecruitmentProcess getSingleRecruitmentProcessDTO(Long recruitmentProcessId) {
        try {
            return recruitmentProcessRepository.findById(recruitmentProcessId)
                    .orElseThrow(() -> new RecruitmentProcessNotFoundException("Recruitment process with id: " + recruitmentProcessId + " not found"));
        } catch (Exception ex) {
            throw throwCorrectException.handleException(ex);
        }
    }
}
