package com.jobrecruitment.controller.common;

import com.jobrecruitment.model.User;
import com.jobrecruitment.model.applicant.Application;
import com.jobrecruitment.model.applicant.SavedJob;
import com.jobrecruitment.model.recruiter.JobPosting;
import com.jobrecruitment.model.recruiter.Recruiter;
import com.jobrecruitment.model.recruiter.RecruiterRole;
import com.jobrecruitment.repository.applicant.ApplicationRepository;
import com.jobrecruitment.repository.applicant.SavedJobRepository;
import com.jobrecruitment.repository.recruiter.JobPostingRepository;
import com.jobrecruitment.repository.recruiter.RecruiterRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    @Autowired
    private JobPostingRepository jobPostingRepository;

    @Autowired
    private RecruiterRepository recruiterRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private SavedJobRepository savedJobRepository;

    @GetMapping("/")
    public ModelAndView home(HttpSession session) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        ModelAndView mav = new ModelAndView("common/index");

        if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return new ModelAndView("redirect:/admin/dashboard");
        }

        User user = (User) session.getAttribute("loggedUser");
        List<JobPosting> jobList;
        Map<Integer, Boolean> appliedMap = new HashMap<>();
        Map<Integer, Boolean> savedMap = new HashMap<>();

        if (user == null || user.getRole().name().equals("APPLICANT")) {
            jobList = jobPostingRepository.findByStatus("Open");

            if (user != null) {
                List<Application> applications = applicationRepository.findByApplicantId(user.getId().intValue());
                Set<Integer> appliedJobIds = applications.stream()
                        .map(app -> app.getJob().getId())
                        .collect(Collectors.toSet());

                for (JobPosting job : jobList) {
                    appliedMap.put(job.getId(), appliedJobIds.contains(job.getId()));
                }

                List<SavedJob> savedJobs = savedJobRepository.findByApplicantId(user.getId());
                for (SavedJob sj : savedJobs) {
                    savedMap.put(sj.getJob().getId(), true);
                }

                mav.addObject("appliedMap", appliedMap);
                mav.addObject("savedMap", savedMap);
            }

        } else if (user.getRole().name().equals("COMPANY")) {
            Integer companyId = user.getCompany() != null ? user.getCompany().getId() : null;
            jobList = companyId != null
                    ? jobPostingRepository.getAllByCompanyId(companyId)
                    : List.of();

        } else if (user.getRole().name().equals("RECRUITER")) {
            Recruiter recruiter = recruiterRepository.findByUserId(user.getId());

            if (recruiter != null) {
                session.setAttribute("currentUserId", user.getId());
                session.setAttribute("isManager", recruiter.getRole() == RecruiterRole.HR_MANAGER);
                session.setAttribute("currentBranchId", recruiter.getBranch().getId());
            }

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

    @GetMapping(value = "/search-live", produces = "text/html")
    public String searchLive(@RequestParam("q") String q, HttpSession session, Model model) {
        List<JobPosting> jobList = (q == null || q.isBlank())
                ? jobPostingRepository.findAll()
                : jobPostingRepository.searchByTitleOrDescription(q);
        model.addAttribute("jobList", jobList);

        User user = (User) session.getAttribute("loggedUser");
        if (user != null && user.getRole().name().equals("APPLICANT")) {
            List<Application> applications = applicationRepository.findByApplicantId(user.getId().intValue());
            Map<Integer, Boolean> appliedMap = new HashMap<>();
            for (Application app : applications) {
                appliedMap.put(app.getJob().getId(), true);
            }
            model.addAttribute("appliedMap", appliedMap);

            List<SavedJob> savedJobs = savedJobRepository.findByApplicantId(user.getId());
            Map<Integer, Boolean> savedMap = new HashMap<>();
            for (SavedJob sj : savedJobs) {
                savedMap.put(sj.getJob().getId(), true);
            }
            model.addAttribute("savedMap", savedMap);
        }

        return "common/job-list-fragment";
    }
}
