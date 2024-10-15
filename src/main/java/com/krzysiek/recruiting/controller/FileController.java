package com.krzysiek.recruiting.controller;

import com.krzysiek.recruiting.dto.BaseResponseDTO;
import com.krzysiek.recruiting.dto.DeleteFileRequestDTO;
import com.krzysiek.recruiting.dto.LoadAllFileResponseDTO;
import com.krzysiek.recruiting.enums.FileType;
import com.krzysiek.recruiting.service.FileService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
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
    public ResponseEntity<BaseResponseDTO> storageNewFile(@RequestParam("file") MultipartFile file, @RequestParam("fileType") FileType fileType) {
        fileService.store(file, fileType);
        return ResponseEntity.status(HttpStatus.CREATED).body(new BaseResponseDTO("Successfully stored file."));
    }

    @DeleteMapping
    public ResponseEntity<BaseResponseDTO> deleteFileByIdAndType(@Valid @RequestBody DeleteFileRequestDTO deleteFileRequestDTO) {
        fileService.delete(deleteFileRequestDTO.fileId(), deleteFileRequestDTO.fileType());
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new BaseResponseDTO("Successfully deleted file."));
    }

    @GetMapping
    public ResponseEntity<LoadAllFileResponseDTO> loadUserFilesList(@RequestParam(name = "page-number", defaultValue = "0") @Min(0) int pageNumber) {
        return ResponseEntity.status(HttpStatus.OK).body(new LoadAllFileResponseDTO("Files", fileService.loadAll(pageNumber)));
    }

    @GetMapping("/resource")
    public ResponseEntity<Resource> loadAsResource(@RequestParam(name = "id") @Min(0) Long fileId) {
        Resource fileResource = fileService.loadAsResource(fileId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileResource.getFilename() + "\"")
                .body(fileResource);
    }

}
