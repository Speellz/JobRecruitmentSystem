package com.jobrecruitment.repository.applicant;

import com.jobrecruitment.model.applicant.Education;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EducationRepository extends JpaRepository<Education, Integer> {
    List<Education> findByUserId(Long userId);
}
