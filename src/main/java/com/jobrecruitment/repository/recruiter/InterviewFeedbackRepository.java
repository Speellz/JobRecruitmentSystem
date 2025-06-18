package com.jobrecruitment.repository.recruiter;

import com.jobrecruitment.model.recruiter.InterviewFeedback;
import com.jobrecruitment.model.recruiter.InterviewSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InterviewFeedbackRepository extends JpaRepository<InterviewFeedback, Integer> {

    Optional<InterviewFeedback> findByInterview(InterviewSchedule interview);

    boolean existsByInterview(InterviewSchedule interview);
}
