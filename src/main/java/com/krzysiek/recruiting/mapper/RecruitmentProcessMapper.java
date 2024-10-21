package com.krzysiek.recruiting.mapper;


import com.krzysiek.recruiting.dto.requestDTOs.RecruitmentProcessRequestDTO;
import com.krzysiek.recruiting.dto.RecruitmentProcessDTO;
import com.krzysiek.recruiting.model.RecruitmentProcess;
import com.krzysiek.recruiting.model.User;
import com.krzysiek.recruiting.model.JobDescription;
import com.krzysiek.recruiting.model.File;
import com.krzysiek.recruiting.service.JobDescriptionServiceServiceImplementation;
import com.krzysiek.recruiting.service.UserService;
import com.krzysiek.recruiting.service.FileService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class RecruitmentProcessMapper {

    @Autowired
    protected UserService userService;

    @Autowired
    protected FileService fileService;

    @Autowired
    protected JobDescriptionServiceServiceImplementation jobDescriptionServiceServiceImplementation;

    @Mapping(source = "userId", target = "user", qualifiedByName = "mapUser")
    @Mapping(source = "jobDescriptionId", target = "jobDescription", qualifiedByName = "mapJobDescription")
    @Mapping(source = "cvId", target = "cv", qualifiedByName = "mapCv")
    @Mapping(source = "recruitmentTaskId", target = "recruitmentTask", qualifiedByName = "mapRecruitmentTask")
    public abstract RecruitmentProcess toEntity(RecruitmentProcessDTO processDTO);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "jobDescription", target = "jobDescriptionDTO")
    @Mapping(source = "cv.id", target = "cvId")
    @Mapping(source = "recruitmentTask.id", target = "recruitmentTaskId")
    public abstract RecruitmentProcessRequestDTO toRecruitmentProcessDTO(RecruitmentProcess process);

    @Named("mapUser")
    protected User mapUser(Long userId) {
        return userService.getUserById(userId);
    }

    @Named("mapJobDescription")
    protected JobDescription mapJobDescription(Long jobDescriptionId) {
        return jobDescriptionServiceServiceImplementation.getJobDescriptionById(jobDescriptionId);
    }

    @Named("mapCv")
    protected File mapCv(Long cvId) {
        if (cvId == null) {
            return null;
        }
        return fileService.getFileById(cvId);
    }

    @Named("mapRecruitmentTask")
    protected File mapRecruitmentTask(Long recruitmentTaskId) {
        if (recruitmentTaskId == null) {
            return null;
        }
        return fileService.getFileById(recruitmentTaskId);
    }
}

