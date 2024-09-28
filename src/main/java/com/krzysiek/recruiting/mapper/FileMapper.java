package com.krzysiek.recruiting.mapper;

import com.krzysiek.recruiting.dto.FileDTO;
import com.krzysiek.recruiting.model.File;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FileMapper {

    @Mapping(source = "user.id", target = "userId")
    FileDTO toDTO(File file);

    @Mapping(source = "userId", target = "user.id")
    File toEntity(FileDTO fileDTO);
}