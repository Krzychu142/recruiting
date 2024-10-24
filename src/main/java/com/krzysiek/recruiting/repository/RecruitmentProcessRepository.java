package com.krzysiek.recruiting.repository;

import io.micrometer.common.lang.NonNullApi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import com.krzysiek.recruiting.model.RecruitmentProcess;

import java.util.Optional;

@NonNullApi
@Repository
public interface RecruitmentProcessRepository extends JpaRepository<RecruitmentProcess, Long>, JpaSpecificationExecutor<RecruitmentProcess> {
    Optional<RecruitmentProcess> findById(Long id);
    int deleteByIdAndUserId(Long id, Long userId);
}
