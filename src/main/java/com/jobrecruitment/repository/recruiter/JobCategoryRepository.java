package com.jobrecruitment.repository.recruiter;

import com.jobrecruitment.model.recruiter.JobCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobCategoryRepository extends JpaRepository<JobCategory, Integer> {

}
