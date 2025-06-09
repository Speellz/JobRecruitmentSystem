package com.jobrecruitment.repository.recruiter;

import com.jobrecruitment.model.recruiter.JobPosting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobPostingRepository extends JpaRepository<JobPosting, Integer> {
    List<JobPosting> findByBranchId(Integer branchId);
    List<JobPosting> findByStatus(String status);
    List<JobPosting> findByCompanyId(Integer companyId);

    @Query("SELECT j FROM JobPosting j WHERE j.branch.id = :branchId")
    List<JobPosting> getAllByBranchId(@Param("branchId") Integer branchId);

    @Query("SELECT j FROM JobPosting j WHERE j.company.id = :companyId")
    List<JobPosting> getAllByCompanyId(@Param("companyId") Integer companyId);

}
