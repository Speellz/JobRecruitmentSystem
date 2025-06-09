package com.jobrecruitment.repository.recruiter;

import com.jobrecruitment.model.recruiter.Recruiter;
import com.jobrecruitment.model.company.Branch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RecruiterRepository extends JpaRepository<Recruiter, Integer> {
    List<Recruiter> findByBranch(Branch branch);
    List<Recruiter> findByBranchId(Integer branchId);
    List<Recruiter> findByCompanyId(Integer companyId);
    Optional<Recruiter> findByUserEmail(String email);
    List<Recruiter> findByBranch_CompanyId(Integer companyId);
    Recruiter findByUserId(Long userId);
}