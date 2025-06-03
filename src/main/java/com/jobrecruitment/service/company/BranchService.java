package com.jobrecruitment.service.company;

import com.jobrecruitment.model.recruiter.Recruiter;
import com.jobrecruitment.model.User;
import com.jobrecruitment.model.company.Branch;
import com.jobrecruitment.repository.company.BranchRepository;
import com.jobrecruitment.repository.recruiter.RecruiterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BranchService {

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private RecruiterRepository recruiterRepository;


    public List<Branch> getBranchesByCompanyId(Integer companyId) {
        return branchRepository.findByCompanyId(companyId);
    }

    public void addBranch(Branch branch) {
        branchRepository.save(branch);
    }

    public void deleteBranch(Integer id) {
        branchRepository.deleteById(id);
    }

    public Branch getBranchById(Integer branchId) {
        return branchRepository.findById(branchId).orElse(null);
    }

    public void updateBranch(Branch updatedBranch) {
        Branch existing = branchRepository.findById(updatedBranch.getId()).orElse(null);
        if (existing != null) {
            existing.setName(updatedBranch.getName());
            existing.setLocation(updatedBranch.getLocation());
            branchRepository.save(existing);
        }
    }

    public void assignManager(Integer branchId, User manager) {
        Branch branch = branchRepository.findById(branchId).orElse(null);
        if (branch != null) {
            branch.setManager(manager);
            branchRepository.save(branch);
        }
    }

    public void saveBranch(Branch branch) {
        branchRepository.save(branch);
    }

    public void assignManager(Integer branchId, Integer recruiterId) {
        Branch branch = branchRepository.findById(branchId).orElseThrow();
        Recruiter recruiter = recruiterRepository.findById(recruiterId).orElseThrow();
        branch.setManager(recruiter.getUser());
        branchRepository.save(branch);
    }

    public void removeManager(Integer branchId) {
        Branch branch = branchRepository.findById(branchId).orElseThrow();
        branch.setManager(null);
        branchRepository.save(branch);
    }


}