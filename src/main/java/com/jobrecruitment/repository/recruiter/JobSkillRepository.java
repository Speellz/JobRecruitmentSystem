package com.jobrecruitment.repository.recruiter;

import com.jobrecruitment.model.recruiter.JobSkill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobSkillRepository extends JpaRepository<JobSkill, Integer> {
    List<JobSkill> findByJobPostingId(Integer jobPostingId);
}

