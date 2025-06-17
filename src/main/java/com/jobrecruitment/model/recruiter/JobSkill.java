package com.jobrecruitment.model.recruiter;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "Job_Skills")
public class JobSkill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = false)
    private JobPosting jobPosting;

    @Column(name = "skill_name", nullable = false)
    private String name;
}
