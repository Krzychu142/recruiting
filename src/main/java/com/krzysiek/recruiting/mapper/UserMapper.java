package com.krzysiek.recruiting.mapper;

import com.krzysiek.recruiting.dto.UserDTO;
import com.krzysiek.recruiting.model.File;
import com.krzysiek.recruiting.model.RecruitmentProcess;
import com.krzysiek.recruiting.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "fileIds", ignore = true)
    @Mapping(target = "recruitmentProcessIds", ignore = true)
    UserDTO toDTO(User user);

    @Mapping(target = "password", ignore = true)
    User toEntity(UserDTO userDTO);

    @Named("fileToId")
    default Set<Long> mapFilesToIds(Set<File> files) {
        return files.stream()
                .map(File::getId)
                .collect(Collectors.toSet());
    }

    @Named("processToId")
    default Set<Long> mapProcessesToIds(Set<RecruitmentProcess> processes) {
        return processes.stream()
                .map(com.krzysiek.recruiting.model.RecruitmentProcess::getId)
                .collect(Collectors.toSet());
    }
}