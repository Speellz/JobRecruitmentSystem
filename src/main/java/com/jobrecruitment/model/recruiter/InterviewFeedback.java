package com.jobrecruitment.model.recruiter;

import com.jobrecruitment.model.User;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "interview_feedback")
public class InterviewFeedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interview_id", nullable = false)
    private InterviewSchedule interview;

    @Lob
    @Column(name = "feedback")
    private String feedback;

    @Column(name = "score")
    private Integer score;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submitted_by")
    private User submittedBy;

    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "result", nullable = false)
    private InterviewResult result;
}
