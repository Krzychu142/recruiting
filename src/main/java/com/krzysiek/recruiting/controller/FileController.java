package com.krzysiek.recruiting.controller;

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
    public ResponseEntity<?> storage(@RequestParam("file") MultipartFile file){
        System.out.println(file);
//        fileService.saveNewFile();
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping
    public ResponseEntity<?> deleteFileById(){
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping
    public ResponseEntity<?> getFileByName(){
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
