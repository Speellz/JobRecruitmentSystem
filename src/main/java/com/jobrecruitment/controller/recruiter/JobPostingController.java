package com.jobrecruitment.controller.recruiter;

import com.jobrecruitment.model.User;
import com.jobrecruitment.model.recruiter.JobCategory;
import com.jobrecruitment.model.recruiter.JobPosting;
import com.jobrecruitment.model.recruiter.Recruiter;
import com.jobrecruitment.model.recruiter.RecruiterRole;
import com.jobrecruitment.repository.recruiter.JobCategoryRepository;
import com.jobrecruitment.repository.recruiter.JobPostingRepository;
import com.jobrecruitment.repository.recruiter.RecruiterRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
                session.setAttribute("currentUserId", user.getId());
                session.setAttribute("isManager", recruiter.getRole() == RecruiterRole.HR_MANAGER);

                if (recruiter.getBranch() != null) {
                    session.setAttribute("currentBranchId", recruiter.getBranch().getId());
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
                                @RequestParam String position,
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
        job.setPosition(position);
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

    @GetMapping("/search-live")
    public String searchLive(@RequestParam("q") String q, Model model) {
        List<JobPosting> jobList = (q == null || q.isBlank())
                ? jobPostingRepository.findAll()
                : jobPostingRepository.searchByTitleOrDescription(q);
        model.addAttribute("jobList", jobList);
        return "common/job-list-fragment";
    }

    @GetMapping("/recruiter/job/{id}/edit")
    public String editJobForm(@PathVariable Integer id, Model model, HttpSession session) {
        Optional<JobPosting> jobOpt = jobPostingRepository.findById(id);
        if (jobOpt.isEmpty()) {
            return "redirect:/?error=Job not found";
        }

        JobPosting job = jobOpt.get();
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) {
            return "redirect:/auth/login?error=Unauthorized";
        }

        if (user.getRole().name().equals("COMPANY")) {
            if (user.getCompany() != null && job.getCompany() != null &&
                    user.getCompany().getId().equals(job.getCompany().getId())) {
                model.addAttribute("jobPosting", job);
                model.addAttribute("categoryList", jobCategoryRepository.findAll());
                return "recruiter/edit-job";
            } else {
                return "redirect:/?error=Not your company job";
            }
        }

        Recruiter recruiter = recruiterRepository.findByUserId(user.getId());
        if (recruiter == null) {
            return "redirect:/?error=Unauthorized";
        }

        boolean isOwner = job.getRecruiter() != null &&
                job.getRecruiter().getId().equals(recruiter.getId());

        boolean isManager = recruiter.getRole() == RecruiterRole.HR_MANAGER;
        boolean isSameBranch = job.getBranch() != null && recruiter.getBranch() != null &&
                job.getBranch().getId().equals(recruiter.getBranch().getId());

        if (!isOwner && !(isManager && isSameBranch)) {
            return "redirect:/?error=Unauthorized access";
        }

        model.addAttribute("jobPosting", job);
        model.addAttribute("categoryList", jobCategoryRepository.findAll());
        return "recruiter/edit-job";
    }

    @PostMapping("/recruiter/job/{id}/update")
    public String updateJob(@PathVariable Integer id,
                            HttpSession session,
                            @RequestParam String title,
                            @RequestParam String description,
                            @RequestParam String position,
                            @RequestParam Integer categoryId,
                            @RequestParam String location,
                            @RequestParam String salaryRange,
                            @RequestParam String employmentType) {

        User user = (User) session.getAttribute("loggedUser");
        if (user == null) return "redirect:/login";

        Optional<JobPosting> jobOpt = jobPostingRepository.findById(id);
        if (jobOpt.isEmpty()) return "redirect:/?error=Job not found";

        JobPosting job = jobOpt.get();

        if (user.getRole().name().equals("COMPANY")) {
            if (!(user.getCompany() != null && job.getCompany() != null &&
                    user.getCompany().getId().equals(job.getCompany().getId()))) {
                return "redirect:/?error=Unauthorized";
            }
        } else {
            Recruiter recruiter = recruiterRepository.findByUserId(user.getId());
            if (recruiter == null) return "redirect:/?error=Unauthorized";

            boolean isOwner = job.getRecruiter() != null &&
                    job.getRecruiter().getId().equals(recruiter.getId());

            boolean isManager = recruiter.getRole() == RecruiterRole.HR_MANAGER;
            boolean isSameBranch = job.getBranch() != null && recruiter.getBranch() != null &&
                    job.getBranch().getId().equals(recruiter.getBranch().getId());

            if (!isOwner && !(isManager && isSameBranch)) {
                return "redirect:/?error=Unauthorized access";
            }
        }

        JobCategory category = jobCategoryRepository.findById(categoryId).orElse(null);
        if (category == null) return "redirect:/?error=Invalid category";

        job.setTitle(title);
        job.setDescription(description);
        job.setPosition(position);
        job.setCategory(category);
        job.setLocation(location);
        job.setSalaryRange(salaryRange);
        job.setEmploymentType(employmentType);
        job.setUpdatedAt(LocalDateTime.now());

        jobPostingRepository.save(job);

        return "redirect:/?success=Job updated";
    }

}
