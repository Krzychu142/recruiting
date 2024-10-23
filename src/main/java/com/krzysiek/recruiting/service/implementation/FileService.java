package com.krzysiek.recruiting.service.implementation;

import com.krzysiek.recruiting.config.StorageProperties;
import com.krzysiek.recruiting.dto.FileDTO;
import com.krzysiek.recruiting.enums.FileType;
import com.krzysiek.recruiting.exception.customExceptions.AccessDeniedException;
import com.krzysiek.recruiting.exception.customExceptions.StorageException;
import com.krzysiek.recruiting.exception.customExceptions.StorageFileNotFoundException;
import com.krzysiek.recruiting.exception.ThrowCorrectException;
import com.krzysiek.recruiting.mapper.FileMapper;
import com.krzysiek.recruiting.model.File;
import com.krzysiek.recruiting.model.User;
import com.krzysiek.recruiting.repository.FileRepository;
import com.krzysiek.recruiting.service.StorageService;
import jakarta.transaction.Transactional;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

            Long ownerId = authenticationService.getLoggedInUserId();
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
            saveFileToDatabase(new FileDTO(uniqueFileName, ownerId, extension, fileType, load(uniqueFileName).toString()));
        } catch (Exception ex) {
            throw throwCorrectException.handleException(ex);
        }
    }

    @Override
    public List<FileDTO> loadAll(int pageNumber) {
        try {
            Pageable pageable = PageRequest.of(pageNumber, 5);
            Page<File> userFilesPage = fileRepository.findByUserId(authenticationService.getLoggedInUserId(), pageable);

            return userFilesPage.getContent().stream()
                    .map(fileMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception ex) {
            throw throwCorrectException.handleException(ex);
        }
    }

    @Override
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    @Override
    public Resource loadAsResource(Long fileId) {
        try {
            FileDTO fileDTO = getFileDTOById(fileId);
            validateUserAccessAndFileExistence(fileDTO);
            Path filePath = Paths.get(fileDTO.getPath()).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                throw new StorageFileNotFoundException("File not found or not readable: " + fileDTO.getPath());
            }

            return resource;
        } catch (Exception ex) {
            throw throwCorrectException.handleException(ex);
        }
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public void delete(Long fileId, FileType fileType) {
        try {
            FileDTO fileDTO = getFileDTOById(fileId);
            validateUserAccessAndFileExistence(fileDTO);
            Path filePath = Path.of(fileDTO.getPath());
            Files.delete(filePath);
            fileRepository.deleteById(fileId);

        } catch (Exception ex) {
            throw throwCorrectException.handleException(ex);
        }
    }

    public File getFileById(Long id) {
        try {
            return fileRepository.findById(id).orElseThrow(() -> new StorageFileNotFoundException("File with id " + id + " not found."));
        } catch (Exception ex) {
            throw throwCorrectException.handleException(ex);
        }
    }

    public void checkIsFileExistsInDatabaseByIdTypeUserId(Long fileId, FileType fileType) {
        Long loggedInUserId = authenticationService.getLoggedInUserId();
        fileRepository.findByIdAndFileTypeAndUserId(fileId, fileType, loggedInUserId)
                .orElseThrow(() -> new StorageFileNotFoundException("File with id: " + fileId + " type: " + fileType + " owned by user with id: " + loggedInUserId + " not found."));
    }

    private FileDTO getFileDTOById(Long fileId) {
        return fileMapper.toDTO(fileRepository.findById(fileId)
                .orElseThrow(() -> new StorageFileNotFoundException("File with provided id not found.")));
    }

    private void validateUserAccessAndFileExistence(FileDTO fileDTO) {
        if (!Objects.equals(fileDTO.getUserId(), authenticationService.getLoggedInUserId())) {
            throw new AccessDeniedException("Access denied - You are not owner of this file.");
        }

        if (!isFileAlreadyExists(fileDTO.getName())) {
            throw new StorageFileNotFoundException("File with provided name not found.");
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

}