package com.krzysiek.recruiting.repository;

import com.krzysiek.recruiting.model.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Long> {
}
