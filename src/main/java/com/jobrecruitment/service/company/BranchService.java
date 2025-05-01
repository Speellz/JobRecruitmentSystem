package com.jobrecruitment.service.company;

import com.jobrecruitment.model.company.Branch;
import com.jobrecruitment.repository.company.BranchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BranchService {

    @Autowired
    private BranchRepository branchRepository;

    public List<Branch> getBranchesByCompanyId(Integer companyId) {
        return branchRepository.findByCompanyId(companyId);
    }

    public void addBranch(Branch branch) {
        branchRepository.save(branch);
    }

    public void deleteBranch(Integer id) {
        branchRepository.deleteById(id);
    }

    public Branch getBranchById(Integer id) {
        return branchRepository.findById(id).orElse(null);
    }

    public void updateBranch(Branch branch) {
        branchRepository.save(branch);
    }
}
