package com.jobrecruitment.service.recruiter;

import com.jobrecruitment.model.recruiter.Recruiter;
import com.jobrecruitment.model.User;
import com.jobrecruitment.model.company.Branch;
import com.jobrecruitment.model.recruiter.RecruiterRole;
import com.jobrecruitment.repository.UserRepository;
import com.jobrecruitment.repository.recruiter.RecruiterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecruiterService {

    @Autowired
    private RecruiterRepository recruiterRepository;

    @Autowired
    private UserRepository userRepository;

    public boolean addRecruiter(Recruiter recruiter) {
        if (userRepository.findByEmail(recruiter.getUser().getEmail()).isPresent()) {
            return false;
        }

        recruiter.getUser().setRole(User.Role.RECRUITER);
        userRepository.save(recruiter.getUser());

        recruiter.setUser(recruiter.getUser());
        recruiter.setCompany(recruiter.getBranch().getCompany());
        recruiter.setRole(RecruiterRole.RECRUITER);

        recruiterRepository.save(recruiter);
        return true;
    }


    public List<Recruiter> getRecruitersByBranch(Branch branch) {
        return recruiterRepository.findByBranch(branch);
    }

    public List<Recruiter> findByCompanyId(Integer companyId) {
        return recruiterRepository.findByCompanyId(companyId);
    }
    public List<Recruiter> findByBranchId(Integer branchId) {
        return recruiterRepository.findByBranchId(branchId);
    }

    public void deleteRecruiter(Integer recruiterId) {
        recruiterRepository.deleteById(recruiterId);
    }

    public Recruiter getRecruiterById(Integer id) {
        return recruiterRepository.findById(id).orElse(null);
    }


}
