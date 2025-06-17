package com.jobrecruitment.repository.applicant;

import com.jobrecruitment.model.applicant.EmploymentHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmploymentHistoryRepository extends JpaRepository<EmploymentHistory, Integer> {
    List<EmploymentHistory> findByUserId(Long userId);
}
