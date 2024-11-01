package com.krzysiek.recruiting.repository;

import com.krzysiek.recruiting.enums.FileType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.krzysiek.recruiting.model.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {
    Page<File> findByUserId(Long userId, Pageable pageable);
    Optional<File> findByIdAndFileTypeAndUserId(Long id, FileType fileType, Long userId);
}
