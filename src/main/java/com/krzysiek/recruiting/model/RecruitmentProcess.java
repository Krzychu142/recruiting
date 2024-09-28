package com.krzysiek.recruiting.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Setter
@Getter
@Entity
@Table(name = "recruitment_processes")
public class RecruitmentProcess {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String companyName;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column
    private LocalDate endDate;

    @Column(precision = 10, scale = 2)
    private BigDecimal offeredSalaryMin;

    @Column(precision = 10, scale = 2)
    private BigDecimal offeredSalaryMax;

    @Column(nullable = false)
    private String position;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String requirements;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cv_id", nullable = false)
    private File cv;

    @Column(nullable = false)
    private Boolean hasRecruitmentTask;

    @Enumerated(EnumType.STRING)
    private RecruitmentTaskStatus recruitmentTaskStatus;

    private LocalDate taskDeadline;

    private LocalDate evaluationDeadline;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RecruitmentProcessStatus status;

}
