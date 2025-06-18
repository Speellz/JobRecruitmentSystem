package com.jobrecruitment.model.recruiter;

import com.jobrecruitment.model.User;
import com.jobrecruitment.model.recruiter.JobPosting;
import com.jobrecruitment.model.applicant.Application;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "interview_schedules")
public class InterviewSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "job_id", nullable = false)
    private JobPosting job;

    @ManyToOne
    @JoinColumn(name = "applicant_id", nullable = false)
    private User applicant;

    @ManyToOne
    @JoinColumn(name = "interviewer_id", nullable = false)
    private User recruiter;

    @OneToOne
    @JoinColumn(name = "application_id")
    private Application application;

    @Column(name = "scheduled_at")
    private LocalDateTime time;

    @Column(name = "status")
    private String status;
}
