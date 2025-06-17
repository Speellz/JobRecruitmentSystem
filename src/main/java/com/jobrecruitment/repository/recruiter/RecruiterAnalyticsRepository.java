package com.jobrecruitment.repository.recruiter;

import com.jobrecruitment.model.recruiter.RecruiterAnalytics;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecruiterAnalyticsRepository extends JpaRepository<RecruiterAnalytics, Integer> {
    RecruiterAnalytics findByRecruiterId(Integer recruiterId);
}
