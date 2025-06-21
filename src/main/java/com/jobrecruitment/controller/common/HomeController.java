package com.jobrecruitment.controller.common;

import com.jobrecruitment.model.User;
import com.jobrecruitment.model.applicant.Application;
import com.jobrecruitment.model.applicant.SavedJob;
import com.jobrecruitment.model.company.Company;
import com.jobrecruitment.model.company.CompanyStatus;
import com.jobrecruitment.model.recruiter.JobPosting;
import com.jobrecruitment.model.recruiter.Recruiter;
import com.jobrecruitment.model.recruiter.RecruiterRole;
import com.jobrecruitment.repository.applicant.ApplicationRepository;
import com.jobrecruitment.repository.applicant.SavedJobRepository;
import com.jobrecruitment.repository.recruiter.JobPostingRepository;
import com.jobrecruitment.repository.recruiter.RecruiterRepository;
import com.jobrecruitment.repository.recruiter.JobCategoryRepository;
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
    private JobCategoryRepository jobCategoryRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private SavedJobRepository savedJobRepository;

    @GetMapping("/")
    public ModelAndView home(@RequestParam(value = "categoryId", required = false) Integer categoryId,
                             HttpSession session) {
        ModelAndView mav = new ModelAndView("common/index");

        User user = (User) session.getAttribute("loggedUser");
        List<JobPosting> jobList = new ArrayList<>();
        Map<Integer, Boolean> appliedMap = new HashMap<>();
        Map<Integer, Boolean> savedMap = new HashMap<>();
        Map<Integer, Boolean> approvedMap = new HashMap<>();
        Map<Integer, Integer> applicationIdMap = new HashMap<>();

        if (user == null || user.getRole().name().equals("APPLICANT")) {
            jobList = jobPostingRepository.findByStatus("Open");

            if (user != null) {
                List<Application> applications = applicationRepository.findByApplicantId(user.getId().intValue());

                for (Application app : applications) {
                    int jobId = app.getJob().getId();
                    appliedMap.put(jobId, true);
                    applicationIdMap.put(jobId, app.getId());

                    if (app.getStatus().name().equals("APPROVED")) {
                        approvedMap.put(jobId, true);
                    }
                }

                List<SavedJob> savedJobs = savedJobRepository.findByApplicantId(user.getId());
                for (SavedJob sj : savedJobs) {
                    savedMap.put(sj.getJob().getId(), true);
                }

                mav.addObject("appliedMap", appliedMap);
                mav.addObject("savedMap", savedMap);
                mav.addObject("approvedMap", approvedMap);
                mav.addObject("applicationIdMap", applicationIdMap);
            }

        } else if (user.getRole().name().equals("COMPANY")) {
            Company company = user.getCompany();
            session.setAttribute("userCompany", company);

            if (company == null) {
                jobList = new ArrayList<>();
            } else if (company.getStatus() == CompanyStatus.APPROVED) {
                jobList = jobPostingRepository.getAllByCompanyId(company.getId());
            } else {
                jobList = new ArrayList<>();
            }

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

        } else if (user.getRole().name().equals("ADMIN")) {
            jobList = jobPostingRepository.findAll();
        }

        if (categoryId != null) {
            jobList = jobList.stream()
                    .filter(j -> j.getCategory() != null && j.getCategory().getId().equals(categoryId))
                    .collect(Collectors.toList());
            mav.addObject("selectedCategoryId", categoryId);
        }

        mav.addObject("jobList", jobList);
        mav.addObject("categoryList", jobCategoryRepository.findAll());
        return mav;
    }

    @GetMapping(value = "/search-live", produces = "text/html")
    public String searchLive(@RequestParam(value = "q", required = false) String q,
                             @RequestParam(value = "categoryId", required = false) Integer categoryId,
                             HttpSession session, Model model) {
        List<JobPosting> jobList = (q == null || q.isBlank())
                ? jobPostingRepository.findAll()
                : jobPostingRepository.searchByTitleOrDescription(q);

        if (categoryId != null) {
            jobList = jobList.stream()
                    .filter(j -> j.getCategory() != null && j.getCategory().getId().equals(categoryId))
                    .collect(Collectors.toList());
        }
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
