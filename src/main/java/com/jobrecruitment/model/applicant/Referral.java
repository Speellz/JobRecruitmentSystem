package com.jobrecruitment.model.applicant;

import com.jobrecruitment.model.User;
import com.jobrecruitment.model.recruiter.JobPosting;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "Referrals")
public class Referral {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "referred_id", nullable = false)
    private User referredUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "referrer_id", nullable = false)
    private User referrerUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = false)
    private JobPosting job;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ReferralStatus status;
}
