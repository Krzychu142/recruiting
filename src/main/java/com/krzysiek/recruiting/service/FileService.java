package com.krzysiek.recruiting.service;

import com.krzysiek.recruiting.config.StorageProperties;
import com.krzysiek.recruiting.dto.FileDTO;
import com.krzysiek.recruiting.dto.UserDTO;
import com.krzysiek.recruiting.enums.FileType;
import com.krzysiek.recruiting.exception.AccessDeniedException;
import com.krzysiek.recruiting.exception.StorageException;
import com.krzysiek.recruiting.exception.StorageFileNotFoundException;
import com.krzysiek.recruiting.exception.ThrowCorrectException;
import com.krzysiek.recruiting.mapper.FileMapper;
import com.krzysiek.recruiting.model.File;
import com.krzysiek.recruiting.model.User;
import com.krzysiek.recruiting.repository.FileRepository;
import jakarta.transaction.Transactional;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.stream.Stream;

@Service
public class FileService implements StorageService {

    private final FileRepository fileRepository;
    private final ThrowCorrectException throwCorrectException;
    private final AuthenticationService authenticationService;
    private final Path rootLocation;
    private final StorageProperties storageProperties;
    private final FileMapper fileMapper;


    public FileService(FileRepository fileRepository, ThrowCorrectException throwCorrectException, AuthenticationService authenticationService, StorageProperties storageProperties, FileMapper fileMapper) {
        this.fileRepository = fileRepository;
        this.throwCorrectException = throwCorrectException;
        this.authenticationService = authenticationService;
        this.rootLocation = Paths.get(storageProperties.getLocation());
        this.storageProperties = storageProperties;
        this.fileMapper = fileMapper;
        init();
    }

    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException ex) {
            throw new RuntimeException("Could not initialize storage", ex);
        }
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public void store(MultipartFile file, FileType fileType) {
        try {
            if (file.isEmpty()) {
                throw new StorageException("File cannot be empty");
            }

            String fileName = file.getOriginalFilename();
            if (fileName == null || fileName.isEmpty()) {
                throw new StorageException("Filename cannot be empty");
            }

            String extension = getFileExtension(file.getOriginalFilename());
            if (!storageProperties.getAllowedExtensions().contains(extension)) {
                throw new StorageException("Bad file extension: " + extension + ". All allowed extensions are: " + storageProperties.getAllowedExtensions());
            }

            Long ownerId = getCurrentUserId();
            String uniqueFileName = createUniqueFileName(fileName, ownerId);
            if (isFileAlreadyExists(uniqueFileName)) {
                throw new StorageException("File already exists");
            }

            Path destinationFile = this.rootLocation.resolve(
                    Paths.get(uniqueFileName))
                    .normalize().toAbsolutePath();

            if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
                throw new StorageException("Destination file path does not match stored location");
            }

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            }
            saveFileToDatabase(new FileDTO(fileName, ownerId, extension, fileType, load(uniqueFileName).toString()));
        } catch (Exception ex) {
            throw throwCorrectException.handleException(ex);
        }
    }

    @Override
    public Stream<Path> loadAll() {
        try (Stream<Path> paths = Files.walk(this.rootLocation, 1)) {
            return paths
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(this.rootLocation::relativize)
                    .toList()
                    .stream();
        } catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }
    }

    @Override
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename) {
        return null;
    }


    @Transactional(rollbackOn = Exception.class)
    @Override
    public void delete(Long fileId, FileType fileType) {
        try {
            FileDTO fileDTO = fileMapper.toDTO(fileRepository.findById(fileId)
                    .orElseThrow(() -> new StorageFileNotFoundException("File with provided id not found.")));
            UserDTO userDTO = authenticationService.getUserDTOFromSecurityContext();

            if (!Objects.equals(fileDTO.getUserId(), userDTO.id())){
                throw new AccessDeniedException("Access denied - You are not owner of this file.");
            }

            // try to delete file from path
            // delete file from database
        } catch (Exception ex) {
            throw throwCorrectException.handleException(ex);
        }
    }

    private String getFileExtension(String filename) {
        String[] parts = filename.split("\\.");
        return parts.length > 1 ? parts[parts.length - 1].toLowerCase() : "";
    }

    private void saveFileToDatabase(FileDTO fileDTO){
        User user = authenticationService.getUserByEmail(authenticationService.getUserDTOFromSecurityContext().email());
        File file = fileMapper.toEntity(fileDTO);
        file.setUser(user);
        fileRepository.save(file);
    }

    private String createUniqueFileName(String filename, Long ownerId) {
        Date dateNow = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String dateString = formatter.format(dateNow);
        return ownerId + "_" + dateString + "_" + filename;
    }

    private Boolean isFileAlreadyExists(String filename) {
        Path filePath = load(filename);
        return Files.exists(filePath);
    }

    private Long getCurrentUserId(){
        return authenticationService.getUserDTOFromSecurityContext().id();
    }

}