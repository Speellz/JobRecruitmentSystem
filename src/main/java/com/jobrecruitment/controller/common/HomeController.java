package com.jobrecruitment.controller.common;

import com.jobrecruitment.model.User;
import com.jobrecruitment.model.recruiter.JobPosting;
import com.jobrecruitment.model.recruiter.Recruiter;
import com.jobrecruitment.repository.recruiter.JobPostingRepository;
import com.jobrecruitment.repository.recruiter.RecruiterRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private JobPostingRepository jobPostingRepository;

    @Autowired
    private RecruiterRepository recruiterRepository;

    @GetMapping("/")
    public ModelAndView home(HttpSession session) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        ModelAndView mav = new ModelAndView("common/index");

        if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return new ModelAndView("redirect:/admin/dashboard");
        }

        User user = (User) session.getAttribute("loggedUser");
        List<JobPosting> jobList;

        if (user == null || user.getRole().name().equals("APPLICANT")) {
            jobList = jobPostingRepository.findByStatus("Open");

        } else if (user.getRole().name().equals("COMPANY")) {
            Integer companyId = user.getCompany() != null ? user.getCompany().getId() : null;
            jobList = companyId != null
                    ? jobPostingRepository.getAllByCompanyId(companyId)
                    : List.of();

        } else if (user.getRole().name().equals("RECRUITER")) {
            Recruiter recruiter = recruiterRepository.findByUserId(user.getId());
            Integer branchId = (recruiter != null && recruiter.getBranch() != null)
                    ? recruiter.getBranch().getId()
                    : null;

            jobList = branchId != null
                    ? jobPostingRepository.getAllByBranchId(branchId)
                    : List.of();

        } else {
            jobList = List.of();
        }

        mav.addObject("jobList", jobList);
        return mav;
    }
}
