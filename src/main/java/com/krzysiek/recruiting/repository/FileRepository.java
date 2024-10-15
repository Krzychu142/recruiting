package com.krzysiek.recruiting.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.krzysiek.recruiting.model.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Long> {
    Page<File> findByUserId(Long userId, Pageable pageable);
}
