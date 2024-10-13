package com.krzysiek.recruiting.service;

import com.krzysiek.recruiting.enums.FileType;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface StorageService {

    void init();
    void store(MultipartFile file, FileType fileType);
    Stream<Path> loadAll();
    Path load(String filename);
    Resource loadAsResource(String filename);
    void delete(Long fileId, FileType fileType);

}
