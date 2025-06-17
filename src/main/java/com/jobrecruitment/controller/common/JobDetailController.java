package com.jobrecruitment.controller.common;

import com.jobrecruitment.model.recruiter.JobPosting;
import com.jobrecruitment.model.recruiter.JobSkill;
import com.jobrecruitment.repository.recruiter.JobPostingRepository;
import com.jobrecruitment.repository.recruiter.JobSkillRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class JobDetailController {

    private final JobPostingRepository jobPostingRepository;
    private final JobSkillRepository jobSkillRepository;

    @GetMapping("/job/{id}")
    public String jobDetail(@PathVariable("id") Integer id, Model model, HttpSession session) {
        JobPosting job = jobPostingRepository.findById(id).orElse(null);
        if (job == null) {
            model.addAttribute("error", "Job not found");
            return "redirect:/";
        }

        List<JobSkill> skills = jobSkillRepository.findByJobPostingId(id);

        model.addAttribute("job", job);
        model.addAttribute("skills", skills);
        return "common/job-detail";
    }
}
