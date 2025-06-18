package com.jobrecruitment.repository.applicant;

import com.jobrecruitment.model.applicant.SavedJob;
import com.jobrecruitment.model.User;
import com.jobrecruitment.model.recruiter.JobPosting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SavedJobRepository extends JpaRepository<SavedJob, Long> {
    List<SavedJob> findByApplicantId(Long applicantId);
    Optional<SavedJob> findByApplicantAndJob(User applicant, JobPosting job);
    void deleteByApplicantAndJob(User applicant, JobPosting job);
}
