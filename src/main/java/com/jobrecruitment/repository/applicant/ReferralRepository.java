package com.jobrecruitment.repository.applicant;

import com.jobrecruitment.model.applicant.Referral;
import com.jobrecruitment.model.User;
import com.jobrecruitment.model.recruiter.JobPosting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReferralRepository extends JpaRepository<Referral, Long> {
    Optional<Referral> findByReferredUserAndJob(User referredUser, JobPosting job);
    List<Referral> findByJob(JobPosting job);

}
