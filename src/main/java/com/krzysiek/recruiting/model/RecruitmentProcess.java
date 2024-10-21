    package com.krzysiek.recruiting.model;

    import com.krzysiek.recruiting.enums.RecruitmentProcessStatus;
    import com.krzysiek.recruiting.enums.RecruitmentTaskStatus;
    import jakarta.persistence.*;
    import lombok.Getter;
    import lombok.Setter;

    import java.time.LocalDate;

    @Setter
    @Getter
    @Entity
    @Table(name = "recruitment_processes",
            uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "job_description_id", "cv_id", "date_of_application"})
    )
    public class RecruitmentProcess {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "user_id", nullable = false)
        private User user;

        @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
        @JoinColumn(name = "job_description_id", referencedColumnName = "id", unique = true, nullable = false)
        private JobDescription jobDescription;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "cv_id", nullable = false)
        private File cv;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "recruitment_task_id", referencedColumnName = "id")
        private File recruitmentTask;

        @Column(name = "date_of_application", nullable = false)
        private LocalDate dateOfApplication = LocalDate.now();

        @Column(name = "process_end_date")
        private LocalDate processEndDate;

        @Column(name = "has_recruitment_task", nullable = false)
        private Boolean hasRecruitmentTask = false;

        @Enumerated(EnumType.STRING)
        @Column(name = "recruitment_task_status")
        private RecruitmentTaskStatus recruitmentTaskStatus = RecruitmentTaskStatus.PENDING;

        @Column(name = "task_deadline")
        private LocalDate taskDeadline;

        @Column(name = "evaluation_deadline")
        private LocalDate evaluationDeadline;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private RecruitmentProcessStatus status;

        public RecruitmentProcess() {}

    }
