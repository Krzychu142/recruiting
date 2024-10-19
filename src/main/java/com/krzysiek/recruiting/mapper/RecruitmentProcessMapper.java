package com.krzysiek.recruiting.mapper;


import com.krzysiek.recruiting.dto.RecruitmentProcessDTO;
import com.krzysiek.recruiting.model.RecruitmentProcess;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RecruitmentProcessMapper {

//    @Mapping(source = "user.id", target = "userId")
//    @Mapping(source = "cv.id", target = "cvId")
//    RecruitmentProcessDTO toDTO(RecruitmentProcess process);
//
//    @Mapping(source = "userId", target = "user.id")
//    @Mapping(source = "cvId", target = "cv.id")
//    RecruitmentProcess toEntity(RecruitmentProcessDTO processDTO);
}

