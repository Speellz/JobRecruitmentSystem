package com.jobrecruitment.controller.common;

import com.jobrecruitment.model.User;
import com.jobrecruitment.model.applicant.Application;
import com.jobrecruitment.model.recruiter.JobPosting;
import com.jobrecruitment.model.recruiter.JobSkill;
import com.jobrecruitment.repository.applicant.ApplicationRepository;
import com.jobrecruitment.repository.recruiter.JobPostingRepository;
import com.jobrecruitment.repository.recruiter.JobSkillRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Controller
@RequiredArgsConstructor
public class JobDetailController {

    private final JobPostingRepository jobPostingRepository;
    private final JobSkillRepository jobSkillRepository;
    private final ApplicationRepository applicationRepository;

    @GetMapping("/job/{id}")
    public String jobDetail(@PathVariable("id") Integer id,
                            Model model,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {

        JobPosting job = jobPostingRepository.findById(id).orElse(null);
        if (job == null) {
            redirectAttributes.addFlashAttribute("error", "Job not found");
            return "redirect:/";
        }

        List<JobSkill> skills = jobSkillRepository.findByJobPostingId(id);
        model.addAttribute("job", job);
        model.addAttribute("skills", skills);

        User user = (User) session.getAttribute("loggedUser");
        if (user != null && user.getRole().name().equals("APPLICANT")) {
            Application application = applicationRepository
                    .findByApplicantIdAndJobId(user.getId(), job.getId().longValue());
            if (application != null) {
                Map<Integer, Boolean> appliedMap = new HashMap<>();
                appliedMap.put(job.getId(), true);
                model.addAttribute("appliedMap", appliedMap);
                model.addAttribute("applicationId", application.getId());
            }
        }

        return "common/job-detail";
    }
}
