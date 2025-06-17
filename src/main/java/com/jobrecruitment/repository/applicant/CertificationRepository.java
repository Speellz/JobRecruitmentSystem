package com.jobrecruitment.repository.applicant;

import com.jobrecruitment.model.applicant.Certification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CertificationRepository extends JpaRepository<Certification, Integer> {
    List<Certification> findByUserId(Long userId);
}
