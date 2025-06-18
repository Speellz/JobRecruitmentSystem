package com.jobrecruitment.model.applicant;

import com.jobrecruitment.model.User;
import com.jobrecruitment.model.recruiter.JobPosting;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "Saved_Jobs")
public class SavedJob {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "applicant_id", nullable = false)
    private User applicant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = false)
    private JobPosting job;

    @Column(name = "saved_at")
    private LocalDateTime savedAt;
}
