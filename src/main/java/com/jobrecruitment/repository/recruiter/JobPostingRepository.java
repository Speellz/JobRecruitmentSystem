package com.jobrecruitment.repository.recruiter;

import com.jobrecruitment.model.recruiter.JobPosting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobPostingRepository extends JpaRepository<JobPosting, Integer> {

    List<JobPosting> findByBranchId(Integer branchId);

    List<JobPosting> findByRecruiterId(Integer recruiterId);

    List<JobPosting> findByStatus(String status);

    List<JobPosting> findByCompanyId(Integer companyId);

    @Query("SELECT j FROM JobPosting j WHERE j.branch.id = :branchId")
    List<JobPosting> getAllByBranchId(@Param("branchId") Integer branchId);

    @Query("SELECT j FROM JobPosting j WHERE j.company.id = :companyId")
    List<JobPosting> getAllByCompanyId(@Param("companyId") Integer companyId);

    @Query(value = """
    SELECT * FROM Job_Postings 
    WHERE 
        title COLLATE Latin1_General_CI_AI LIKE CONCAT('%', :keyword, '%') OR 
        description COLLATE Latin1_General_CI_AI LIKE CONCAT('%', :keyword, '%') OR
        position COLLATE Latin1_General_CI_AI LIKE CONCAT('%', :keyword, '%') OR
        location COLLATE Latin1_General_CI_AI LIKE CONCAT('%', :keyword, '%') OR
        employment_type COLLATE Latin1_General_CI_AI LIKE CONCAT('%', :keyword, '%')
    """, nativeQuery = true)
    List<JobPosting> searchByTitleOrDescription(@Param("keyword") String keyword);


    @Query("SELECT j FROM JobPosting j JOIN FETCH j.recruiter r JOIN FETCH r.branch WHERE j.id = :id")
    Optional<JobPosting> findWithRecruiterAndBranchById(Integer id);

    @Query("SELECT COUNT(j) FROM JobPosting j WHERE j.branch.company.id = :companyId")
    int countByBranchCompanyId(@Param("companyId") Integer companyId);
}
