package com.jobrecruitment.repository.applicant;

import com.jobrecruitment.model.applicant.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Integer> {
    List<Application> findByApplicantId(Integer applicantId);
    List<Application> findByJobId(Integer jobId);

    @Query("SELECT COUNT(a) > 0 FROM Application a WHERE a.job.id = :jobId AND a.applicant.id = :applicantId")
    boolean existsByJobIdAndApplicantId(@Param("jobId") Integer jobId, @Param("applicantId") Long applicantId);
}
