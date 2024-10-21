package com.krzysiek.recruiting.repository;

import io.micrometer.common.lang.NonNullApi;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.krzysiek.recruiting.model.RecruitmentProcess;

import java.util.Optional;

@NonNullApi
@Repository
public interface RecruitmentProcessRepository extends JpaRepository<RecruitmentProcess, Long> {
    Page<RecruitmentProcess> findAllByUserId(Long userId, Pageable pageable);
    Optional<RecruitmentProcess> findById(Long id);
}
