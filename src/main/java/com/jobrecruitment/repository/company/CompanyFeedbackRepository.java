package com.jobrecruitment.repository.company;

import com.jobrecruitment.model.company.CompanyReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CompanyFeedbackRepository extends JpaRepository<CompanyReview, Long> {

    List<CompanyReview> findByCompanyId(Long companyId);
    Optional<CompanyReview> findByCompanyIdAndUserId(Long companyId, Long userId);
    boolean existsByCompanyIdAndUserId(Long companyId, Long userId);

}
