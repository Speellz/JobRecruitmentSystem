package com.jobrecruitment.controller.admin;

import com.jobrecruitment.model.User;
import com.jobrecruitment.model.recruiter.JobCategory;
import com.jobrecruitment.model.recruiter.JobPosting;
import com.jobrecruitment.repository.applicant.ApplicationRepository;
import com.jobrecruitment.repository.applicant.ReferralRepository;
import com.jobrecruitment.repository.recruiter.JobCategoryRepository;
import com.jobrecruitment.repository.recruiter.JobPostingRepository;
import com.jobrecruitment.repository.recruiter.JobSkillRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/job")
@RequiredArgsConstructor
public class AdminJobController {

    private final JobPostingRepository jobPostingRepository;
    private final JobCategoryRepository jobCategoryRepository;
    private final JobSkillRepository jobSkillRepository;
    private final ApplicationRepository applicationRepository;
    private final ReferralRepository referralRepository;

    @GetMapping("/{id}/edit")
    public String editJob(@PathVariable Integer id, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        User admin = (User) session.getAttribute("loggedUser");
        if (admin == null || !admin.getRole().name().equals("ADMIN")) {
            return "redirect:/auth/login";
        }

        JobPosting job = jobPostingRepository.findById(id).orElse(null);
        if (job == null) {
            redirectAttributes.addFlashAttribute("error", "Job not found.");
            return "redirect:/";
        }

        List<JobCategory> categoryList = jobCategoryRepository.findAll();
        model.addAttribute("jobPosting", job);
        model.addAttribute("categoryList", categoryList);
        return "admin/edit-job";
    }

    @PostMapping("/{id}/delete")
    public String deleteJob(@PathVariable Integer id, HttpSession session, RedirectAttributes redirectAttributes) {
        User admin = (User) session.getAttribute("loggedUser");
        if (admin == null || !admin.getRole().name().equals("ADMIN")) {
            return "redirect:/auth/login";
        }

        JobPosting job = jobPostingRepository.findById(id).orElse(null);
        if (job == null) {
            redirectAttributes.addFlashAttribute("error", "Job not found.");
            return "redirect:/";
        }

        applicationRepository.deleteAll(applicationRepository.findByJobId(job.getId()));
        jobSkillRepository.deleteAll(jobSkillRepository.findByJobPostingId(job.getId()));
        referralRepository.deleteAll(referralRepository.findByJob(job));

        jobPostingRepository.delete(job);

        redirectAttributes.addFlashAttribute("success", "Job deleted successfully.");
        return "redirect:/";
    }


    @PostMapping("/{id}/edit")
    public String updateJob(@PathVariable Integer id,
                            @RequestParam String title,
                            @RequestParam String position,
                            @RequestParam String description,
                            @RequestParam String location,
                            @RequestParam String salaryRange,
                            @RequestParam String employmentType,
                            @RequestParam Integer categoryId,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {

        User admin = (User) session.getAttribute("loggedUser");
        if (admin == null || !admin.getRole().name().equals("ADMIN")) {
            return "redirect:/auth/login";
        }

        JobPosting job = jobPostingRepository.findById(id).orElse(null);
        if (job == null) {
            redirectAttributes.addFlashAttribute("error", "Job not found.");
            return "redirect:/";
        }

        job.setTitle(title);
        job.setPosition(position);
        job.setDescription(description);
        job.setLocation(location);
        job.setSalaryRange(salaryRange);
        job.setEmploymentType(employmentType);

        job.setCategory(jobCategoryRepository.findById(categoryId).orElse(null));
        job.setUpdatedAt(java.time.LocalDateTime.now());

        jobPostingRepository.save(job);

        redirectAttributes.addFlashAttribute("success", "Job updated successfully.");
        return "redirect:/";
    }

    @GetMapping("/{id}/applications")
    public String viewApplications(@PathVariable Integer id, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        User admin = (User) session.getAttribute("loggedUser");
        if (admin == null || !admin.getRole().name().equals("ADMIN")) {
            return "redirect:/auth/login";
        }

        JobPosting job = jobPostingRepository.findById(id).orElse(null);
        if (job == null) {
            redirectAttributes.addFlashAttribute("error", "Job not found.");
            return "redirect:/";
        }

        model.addAttribute("job", job);
        model.addAttribute("applications", applicationRepository.findByJobId(id));
        return "admin/job-applications";
    }


}
