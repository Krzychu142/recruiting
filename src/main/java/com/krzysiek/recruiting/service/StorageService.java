package com.krzysiek.recruiting.service;

import com.krzysiek.recruiting.dto.FileDTO;
import com.krzysiek.recruiting.enums.FileType;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;

public interface StorageService {

    void init();
    void store(MultipartFile file, FileType fileType);
    List<FileDTO> loadAll(int pageNumber);
    Path load(String filename);
    Resource loadAsResource(Long fileId);
    void delete(Long fileId, FileType fileType);

}
