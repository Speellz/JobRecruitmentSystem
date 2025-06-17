package com.jobrecruitment.repository.applicant;

import com.jobrecruitment.model.applicant.UserSkill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserSkillRepository extends JpaRepository<UserSkill, Integer> {
    List<UserSkill> findByUserId(Long userId);
}
