package com.jobrecruitment.controller.recruiter;

import com.jobrecruitment.model.User;
import com.jobrecruitment.model.recruiter.JobCategory;
import com.jobrecruitment.model.recruiter.JobPosting;
import com.jobrecruitment.model.recruiter.Recruiter;
import com.jobrecruitment.repository.recruiter.JobCategoryRepository;
import com.jobrecruitment.repository.recruiter.JobPostingRepository;
import com.jobrecruitment.repository.recruiter.RecruiterRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class JobPostingController {

    @Autowired
    private JobPostingRepository jobPostingRepository;

    @Autowired
    private RecruiterRepository recruiterRepository;

    @Autowired
    private JobCategoryRepository jobCategoryRepository;


    @GetMapping("/jobs")
    public String index(Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        List<JobPosting> jobList;

        if (user == null || user.getRole().equals("APPLICANT")) {
            jobList = jobPostingRepository.findAll();
        } else {
            Recruiter recruiter = recruiterRepository.findByUserId(user.getId());

            if (recruiter != null) {
                if (recruiter.getBranch() != null) {
                    jobList = jobPostingRepository.getAllByBranchId(recruiter.getBranch().getId());
                } else if (recruiter.getCompany() != null) {
                    jobList = jobPostingRepository.getAllByCompanyId(recruiter.getCompany().getId());
                } else {
                    jobList = List.of();
                }
            } else {
                jobList = List.of();
            }
        }

        model.addAttribute("jobList", jobList);
        return "index";
    }

    @GetMapping("/recruiter/job/form")
    public String showJobForm(Model model) {
        List<JobCategory> categoryList = jobCategoryRepository.findAll();
        model.addAttribute("categoryList", categoryList);
        return "recruiter/job-form";
    }


    @PostMapping("/recruiter/job/add")
    public String addJobPosting(HttpSession session,
                                @RequestParam String title,
                                @RequestParam String description,
                                @RequestParam Integer categoryId,
                                @RequestParam String location,
                                @RequestParam String salaryRange,
                                @RequestParam String employmentType) {

        User loggedUser = (User) session.getAttribute("loggedUser");
        if (loggedUser == null) return "redirect:/login";

        Recruiter recruiter = recruiterRepository.findByUserId(loggedUser.getId());
        if (recruiter == null || recruiter.getBranch() == null || recruiter.getCompany() == null) {
            return "redirect:/recruiter/index";
        }

        JobCategory category = jobCategoryRepository.findById(categoryId).orElse(null);
        if (category == null) {
            return "redirect:/recruiter/job/form?error=invalidCategory";
        }

        JobPosting job = new JobPosting();
        job.setTitle(title);
        job.setDescription(description);
        job.setLocation(location);
        job.setSalaryRange(salaryRange);
        job.setEmploymentType(employmentType);
        job.setStatus("Open");
        job.setCreatedAt(LocalDateTime.now());
        job.setUpdatedAt(LocalDateTime.now());
        job.setBranch(recruiter.getBranch());
        job.setCompany(recruiter.getCompany());
        job.setCategory(category);
        job.setRecruiter(recruiter);

        jobPostingRepository.save(job);

        return "redirect:/";
    }

}
