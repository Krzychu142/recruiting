package com.krzysiek.recruiting.mapper;

import com.krzysiek.recruiting.dto.JobDescriptionDTO;
import com.krzysiek.recruiting.model.JobDescription;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface JobDescriptionMapper {
    @Mapping(target = "id", ignore = true)
    JobDescription toEntity(JobDescriptionDTO jobDescriptionDTO);
}
