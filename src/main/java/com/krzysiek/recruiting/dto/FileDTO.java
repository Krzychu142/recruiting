package com.krzysiek.recruiting.dto;

import com.krzysiek.recruiting.enums.FileType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class FileDTO{
    private MultipartFile file;
    private FileType type;
    private String name;
    private String link;
    private Long userId;

    public FileDTO() {}
}