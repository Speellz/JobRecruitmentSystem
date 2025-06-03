package com.jobrecruitment.repository.company;

import com.jobrecruitment.model.company.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BranchRepository extends JpaRepository<Branch, Integer> {
    List<Branch> findByCompanyId(Integer companyId);
}
