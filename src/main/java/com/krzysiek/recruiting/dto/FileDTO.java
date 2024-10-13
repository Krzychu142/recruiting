package com.krzysiek.recruiting.dto;

import com.krzysiek.recruiting.enums.FileType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileDTO{
    private String name;
    private Long userId;
    private String extension;
    private FileType fileType;
    private String path;

    public FileDTO() {}

    public FileDTO(String name, Long userId, String extension, FileType fileType, String path) {
        this.name = name;
        this.userId = userId;
        this.extension = extension;
        this.fileType = fileType;
        this.path = path;
    }
}