package com.krzysiek.recruiting.mapper;


import com.krzysiek.recruiting.dto.RecruitmentProcessDTO;
import com.krzysiek.recruiting.model.RecruitmentProcess;
import com.krzysiek.recruiting.service.FileService;
import com.krzysiek.recruiting.service.JobDescriptionServiceServiceImplementation;
import com.krzysiek.recruiting.service.UserService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserService.class, JobDescriptionServiceServiceImplementation.class, FileService.class})
public interface RecruitmentProcessMapper {

//    @Mapping(source = "user.id", target = "userId")
//    @Mapping(source = "cv.id", target = "cvId")
//    RecruitmentProcessDTO toDTO(RecruitmentProcess process);
//
    @Mapping(source = "userId", target = "user")
    @Mapping(source = "jobDescriptionId", target = "jobDescription")
    @Mapping(source = "cvId", target = "cv")
    @Mapping(source = "recruitmentTaskId", target = "recruitmentTask")
    RecruitmentProcess toEntity(RecruitmentProcessDTO processDTO);

}

