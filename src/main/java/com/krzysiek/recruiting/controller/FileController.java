package com.krzysiek.recruiting.controller;

import com.krzysiek.recruiting.dto.BaseResponseDTO;
import com.krzysiek.recruiting.dto.DeleteFileRequestDTO;
import com.krzysiek.recruiting.dto.LoadAllFileResponseDTO;
import com.krzysiek.recruiting.enums.FileType;
import com.krzysiek.recruiting.service.FileService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
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
    public ResponseEntity<BaseResponseDTO> loadUserFilesList(@RequestParam(name = "page-number", defaultValue = "0") @Min(0) int pageNumber) {
        return ResponseEntity.status(HttpStatus.OK).body(new LoadAllFileResponseDTO("Files", fileService.loadAll(pageNumber)));
    }

//    @GetMapping
//    public ResponseEntity<?> getFileByNameAndType() {
//        return ResponseEntity.status(HttpStatus.OK).build();
//    }
//
//    //TODO: getAllUserFiles - or list of file - make it pageable!
//    @GetMapping("/all")
//    public ResponseEntity<?> getAllUserFiles() {
//        return ResponseEntity.status(HttpStatus.OK).build();
//    }


}
