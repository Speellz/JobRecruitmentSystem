package com.jobrecruitment.model.recruiter;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "Recruiter_Analytics")
public class RecruiterAnalytics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "recruiter_id")
    private Recruiter recruiter;

    @Column(name = "total_postings")
    private Integer totalPosts;

    @Column(name = "total_hires")
    private Integer totalHires;

    @Column(name = "average_hiring_time")
    private Integer averageHiringTime;
}
