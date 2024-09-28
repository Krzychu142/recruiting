package com.krzysiek.recruiting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.krzysiek.recruiting.model.RecruitmentProcess;

@Repository
public interface RecruitmentProcessRepository extends JpaRepository<RecruitmentProcess, Long> {}
