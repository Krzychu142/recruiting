package com.krzysiek.recruiting.service;

import com.krzysiek.recruiting.dto.BaseResponseDTO;
import com.krzysiek.recruiting.dto.UserDTO;
import com.krzysiek.recruiting.exception.ThrowCorrectException;
import com.krzysiek.recruiting.repository.FileRepository;
import org.springframework.stereotype.Service;

@Service
public class FileService {

    private final FileRepository fileRepository;
    private final ThrowCorrectException throwCorrectException;
    private final AuthenticationService authenticationService;

    public FileService(FileRepository fileRepository, ThrowCorrectException throwCorrectException, AuthenticationService authenticationService) {
        this.fileRepository = fileRepository;
        this.throwCorrectException = throwCorrectException;
        this.authenticationService = authenticationService;
    }

    public BaseResponseDTO saveNewFile(){
        try {
            UserDTO userDTO = authenticationService.getUserDTOFromSecurityContext();
            System.out.println("User DTO: " + userDTO);
            return new BaseResponseDTO("File successfully saved.");
        } catch (Exception ex) {
            throw throwCorrectException.handleException(ex);
        }
    }

}