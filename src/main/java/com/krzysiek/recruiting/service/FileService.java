package com.krzysiek.recruiting.service;

import com.krzysiek.recruiting.config.StorageProperties;
import com.krzysiek.recruiting.exception.StorageException;
import com.krzysiek.recruiting.exception.ThrowCorrectException;
import com.krzysiek.recruiting.repository.FileRepository;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

@Service
public class FileService implements StorageService {

    private final FileRepository fileRepository;
    private final ThrowCorrectException throwCorrectException;
    private final AuthenticationService authenticationService;
    private final Path rootLocation;
    private final StorageProperties storageProperties;


    public FileService(FileRepository fileRepository, ThrowCorrectException throwCorrectException, AuthenticationService authenticationService, StorageProperties storageProperties) {
        this.fileRepository = fileRepository;
        this.throwCorrectException = throwCorrectException;
        this.authenticationService = authenticationService;
        this.rootLocation = Paths.get(storageProperties.getLocation());
        this.storageProperties = storageProperties;
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

    @Override
    public void store(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new StorageException("File cannot be empty");
            }
            String fileName = file.getOriginalFilename();
            if (fileName == null || fileName.isEmpty()) {
                throw new StorageException("Filename cannot be empty");
            }
            String extension = getFileExtension(file.getOriginalFilename());
            if (!storageProperties.getAllowedExtensions().contains(getFileExtension(extension))) {
                throw new StorageException("Bad file extension: " + extension + ". All allowed extensions are: " + storageProperties.getAllowedExtensions());
            }
            Path destinationFile = this.rootLocation.resolve(
                    Paths.get(fileName))
                    .normalize().toAbsolutePath();

            if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
                throw new StorageException("Destination file path does not match stored location");
            }

            // createCustomFileName - based on o.g. file name and user (owner) email and data of creation?
            // check is file already exist - if true throw exception to prevent replace file

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            }

        } catch (Exception ex) {
            throw throwCorrectException.handleException(ex);
        }
    }

    @Override
    public Stream<Path> loadAll() {
        return Stream.empty();
    }

    @Override
    public Path load(String filename) {
        return null;
    }

    @Override
    public Resource loadAsResource(String filename) {
        return null;
    }

    @Override
    public void delete(String filename) {

    }

    private String getFileExtension(String filename) {
        String[] parts = filename.split("\\.");
        return parts.length > 1 ? parts[parts.length - 1].toLowerCase() : "";
    }

    private String createUniqueFileName(String filename) {
        return "Final custom name";
    }

    private Boolean isFileAlreadyExists(){
        return false;
    }

}