package com.krzysiek.recruiting.model;

import com.krzysiek.recruiting.enums.ContractType;
import com.krzysiek.recruiting.enums.WorkLocation;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "job_descriptions")
public class JobDescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String companyName;

    @Column(nullable = false)
    private String jobTitle;

    @Column
    private String companyAddress;

    @Enumerated(EnumType.STRING)
    @Column(name = "work_location", nullable = false)
    private WorkLocation workLocation;

    @Column
    private ContractType contractType;

    @Column(columnDefinition = "TEXT")
    private String requirements;

    @Column(precision = 10, scale = 2)
    private BigDecimal minRate;

    @Column(precision = 10, scale = 2)
    private BigDecimal maxRate;

    @OneToOne(mappedBy = "jobDescription")
    private RecruitmentProcess recruitmentProcess;

    public JobDescription() {}

}
