package com.jobrecruitment.controller.recruiter;

import com.jobrecruitment.model.User;
import com.jobrecruitment.model.applicant.Application;
import com.jobrecruitment.model.recruiter.*;
import com.jobrecruitment.repository.recruiter.JobCategoryRepository;
import com.jobrecruitment.repository.recruiter.JobPostingRepository;
import com.jobrecruitment.repository.recruiter.JobSkillRepository;
import com.jobrecruitment.repository.recruiter.RecruiterRepository;
import com.jobrecruitment.repository.applicant.ApplicationRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class JobPostingController {

    @Autowired
    private JobPostingRepository jobPostingRepository;

    @Autowired
    private RecruiterRepository recruiterRepository;

    @Autowired
    private JobCategoryRepository jobCategoryRepository;

    @Autowired
    private ApplicationRepository ApplicationRepository;

    @Autowired
    private JobSkillRepository jobSkillRepository;


    @PostMapping("/recruiter/job/add")
    public String addJobPosting(HttpSession session,
                                @RequestParam String title,
                                @RequestParam String description,
                                @RequestParam String position,
                                @RequestParam Integer categoryId,
                                @RequestParam String location,
                                @RequestParam String salaryRange,
                                @RequestParam String employmentType,
                                @RequestParam(required = false) String skills,
                                RedirectAttributes redirectAttributes) {

        User loggedUser = (User) session.getAttribute("loggedUser");
        if (loggedUser == null) return "redirect:/login";

        Recruiter recruiter = recruiterRepository.findByUserId(loggedUser.getId());
        if (recruiter == null || recruiter.getBranch() == null || recruiter.getCompany() == null) {
            redirectAttributes.addFlashAttribute("error", "Invalid recruiter or branch/company.");
            return "redirect:/recruiter/job/form";
        }

        JobCategory category = jobCategoryRepository.findById(categoryId).orElse(null);
        if (category == null) {
            redirectAttributes.addFlashAttribute("error", "Invalid category selected.");
            return "redirect:/recruiter/job/form";
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

        if (skills != null && !skills.isBlank()) {
            String[] skillArray = skills.split(",");
            for (String skillName : skillArray) {
                JobSkill skill = new JobSkill();
                skill.setJobPosting(job);
                skill.setName(skillName.trim());
                jobSkillRepository.save(skill);
            }
        }

        redirectAttributes.addFlashAttribute("success", "Job successfully posted.");
        return "redirect:/";
    }


    @GetMapping(value = "/search-live", produces = "text/html")
    public String searchLive(@RequestParam("q") String q, HttpSession session, Model model) {
        List<JobPosting> jobList = (q == null || q.isBlank())
                ? jobPostingRepository.findAll()
                : jobPostingRepository.searchByTitleOrDescription(q);
        model.addAttribute("jobList", jobList);

        User user = (User) session.getAttribute("loggedUser");
        if (user != null && user.getRole().name().equals("APPLICANT")) {
            List<Application> applications = ApplicationRepository.findByApplicantId(user.getId().intValue());

            Map<Integer, Boolean> appliedMap = new HashMap<>();
            for (Application app : applications) {
                appliedMap.put(app.getJob().getId(), true);
            }

            model.addAttribute("appliedMap", appliedMap);
        }

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
                            @RequestParam String employmentType,
                            RedirectAttributes redirectAttributes) {

        User user = (User) session.getAttribute("loggedUser");
        if (user == null) return "redirect:/login";

        Optional<JobPosting> jobOpt = jobPostingRepository.findById(id);
        if (jobOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Job not found.");
            return "redirect:/";
        }

        JobPosting job = jobOpt.get();

        if (user.getRole().name().equals("COMPANY")) {
            if (!(user.getCompany() != null && job.getCompany() != null &&
                    user.getCompany().getId().equals(job.getCompany().getId()))) {
                redirectAttributes.addFlashAttribute("error", "Unauthorized.");
                return "redirect:/";
            }
        } else {
            Recruiter recruiter = recruiterRepository.findByUserId(user.getId());
            if (recruiter == null) {
                redirectAttributes.addFlashAttribute("error", "Unauthorized.");
                return "redirect:/";
            }

            boolean isOwner = job.getRecruiter() != null &&
                    job.getRecruiter().getId().equals(recruiter.getId());

            boolean isManager = recruiter.getRole() == RecruiterRole.HR_MANAGER;
            boolean isSameBranch = job.getBranch() != null && recruiter.getBranch() != null &&
                    job.getBranch().getId().equals(recruiter.getBranch().getId());

            if (!isOwner && !(isManager && isSameBranch)) {
                redirectAttributes.addFlashAttribute("error", "Unauthorized access.");
                return "redirect:/";
            }
        }

        JobCategory category = jobCategoryRepository.findById(categoryId).orElse(null);
        if (category == null) {
            redirectAttributes.addFlashAttribute("error", "Invalid category.");
            return "redirect:/";
        }

        job.setTitle(title);
        job.setDescription(description);
        job.setPosition(position);
        job.setCategory(category);
        job.setLocation(location);
        job.setSalaryRange(salaryRange);
        job.setEmploymentType(employmentType);
        job.setUpdatedAt(LocalDateTime.now());

        jobPostingRepository.save(job);

        redirectAttributes.addFlashAttribute("success", "Job successfully updated.");
        return "redirect:/";
    }

    @GetMapping("/recruiter/jobs/my-branch")
    public String viewMyBranchJobs(Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) return "redirect:/auth/login";

        Recruiter recruiter = recruiterRepository.findByUserId(user.getId());
        if (recruiter == null || recruiter.getBranch() == null) {
            model.addAttribute("error", "Branch not found.");
            return "common/index";
        }

        List<JobPosting> jobList = jobPostingRepository.findByBranchId(recruiter.getBranch().getId());
        model.addAttribute("jobList", jobList);
        model.addAttribute("viewMode", "branch");

        return "common/index";
    }

    @GetMapping("/recruiter/jobs/my")
    public String viewMyJobs(Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) return "redirect:/auth/login";

        Recruiter recruiter = recruiterRepository.findByUserId(user.getId());
        if (recruiter == null) {
            model.addAttribute("error", "Recruiter not found.");
            return "common/index";
        }

        List<JobPosting> jobList = jobPostingRepository.findByRecruiterId(recruiter.getId());
        model.addAttribute("jobList", jobList);
        model.addAttribute("viewMode", "personal");

        return "common/index";
    }

    @GetMapping("/recruiter/job/form")
    public String showJobForm(Model model, HttpSession session) {
        List<JobCategory> categoryList = jobCategoryRepository.findAll();
        model.addAttribute("categoryList", categoryList);
        return "recruiter/job-form";
    }

    @PostMapping("/recruiter/job/{id}/delete")
    public String deleteJobPosting(@PathVariable Integer id,
                                   HttpSession session,
                                   RedirectAttributes redirectAttributes) {

        User user = (User) session.getAttribute("loggedUser");
        if (user == null) return "redirect:/login";

        JobPosting job = jobPostingRepository.findById(id).orElse(null);
        if (job == null) {
            redirectAttributes.addFlashAttribute("error", "Job not found.");
            return "redirect:/";
        }

        Recruiter recruiter = recruiterRepository.findByUserId(user.getId());
        boolean isManager = recruiter != null && recruiter.getRole().name().equals("HR_MANAGER");
        boolean canDelete = job.getRecruiter().getUser().getId().equals(user.getId()) ||
                (isManager && recruiter.getBranch().getId().equals(job.getBranch().getId()));

        if (!canDelete) {
            redirectAttributes.addFlashAttribute("error", "You do not have permission to delete this job.");
            return "redirect:/";
        }

        jobSkillRepository.deleteAll(jobSkillRepository.findByJobPostingId(job.getId()));

        jobPostingRepository.delete(job);

        redirectAttributes.addFlashAttribute("success", "Job deleted successfully.");
        return "redirect:/";
    }

}
