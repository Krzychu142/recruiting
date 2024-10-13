package com.krzysiek.recruiting.controller;

import com.krzysiek.recruiting.dto.BaseResponseDTO;
import com.krzysiek.recruiting.enums.FileType;
import com.krzysiek.recruiting.service.FileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/file")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping
    public ResponseEntity<BaseResponseDTO> storage(@RequestParam("file") MultipartFile file, @RequestParam("fileType") FileType fileType) {
        fileService.store(file, fileType);
        return ResponseEntity.status(HttpStatus.CREATED).body(new BaseResponseDTO("Successfully stored file."));
    }

    @DeleteMapping
    public ResponseEntity<?> deleteFileById() {
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping
    public ResponseEntity<?> getFileByName() {
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
