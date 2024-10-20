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
@Table(name = "job_descriptions",
uniqueConstraints = @UniqueConstraint(columnNames = {"company_name", "job_title", "requirements"}))
public class JobDescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_name", nullable = false)
    private String companyName;

    @Column(name = "job_title", nullable = false)
    private String jobTitle;

    @Column(name = "company_address")
    private String companyAddress;

    @Enumerated(EnumType.STRING)
    @Column(name = "work_location", nullable = false)
    private WorkLocation workLocation;

    @Column(name = "contract_type")
    private ContractType contractType;

    @Column(name = "requirements", columnDefinition = "TEXT")
    private String requirements;

    @Column(name = "min_rate", precision = 10, scale = 2)
    private BigDecimal minRate;

    @Column(name = "max_rate", precision = 10, scale = 2)
    private BigDecimal maxRate;

    @OneToOne(mappedBy = "jobDescription")
    private RecruitmentProcess recruitmentProcess;

    public JobDescription() {}

}
