package com.jobrecruitment.controller.recruiter;

import com.jobrecruitment.model.User;
import com.jobrecruitment.model.applicant.Application;
import com.jobrecruitment.model.applicant.ApplicationStatus;
import com.jobrecruitment.model.recruiter.JobPosting;
import com.jobrecruitment.model.recruiter.Recruiter;
import com.jobrecruitment.repository.applicant.ApplicationRepository;
import com.jobrecruitment.repository.recruiter.JobPostingRepository;
import com.jobrecruitment.repository.recruiter.RecruiterRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/recruiter")
public class RecruiterApplicationController {

    private final JobPostingRepository jobPostingRepository;
    private final ApplicationRepository applicationRepository;
    private final RecruiterRepository recruiterRepository;

    @GetMapping("/job/{jobId}/applications")
    public String viewApplications(@PathVariable Integer jobId, HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null || !user.getRole().name().equals("RECRUITER")) {
            return "redirect:/login";
        }

        Recruiter recruiter = recruiterRepository.findByUserId(user.getId());
        JobPosting job = jobPostingRepository.findById(jobId).orElse(null);

        if (job == null || !job.getBranch().getId().equals(recruiter.getBranch().getId())) {
            return "redirect:/recruiter/index";
        }

        List<Application> applications = applicationRepository.findByJobId(jobId);

        model.addAttribute("job", job);
        model.addAttribute("applications", applications);
        return "recruiter/applications";
    }

    @PostMapping("/application/{id}/approve")
    public String approveApplication(@PathVariable Integer id, HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null || !user.getRole().name().equals("RECRUITER")) {
            return "redirect:/login";
        }

        Application app = applicationRepository.findById(id).orElse(null);
        if (app == null) return "redirect:/recruiter/index";

        Recruiter recruiter = recruiterRepository.findByUserId(user.getId());
        if (!app.getJob().getBranch().getId().equals(recruiter.getBranch().getId())) {
            return "redirect:/recruiter/index";
        }

        app.setStatus(ApplicationStatus.APPROVED);
        applicationRepository.save(app);

        return "redirect:/recruiter/job/" + app.getJob().getId() + "/applications";
    }

    @PostMapping("/application/{id}/reject")
    public String rejectApplication(@PathVariable Integer id, HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null || !user.getRole().name().equals("RECRUITER")) {
            return "redirect:/login";
        }

        Application app = applicationRepository.findById(id).orElse(null);
        if (app == null) return "redirect:/recruiter/index";

        Recruiter recruiter = recruiterRepository.findByUserId(user.getId());
        if (!app.getJob().getBranch().getId().equals(recruiter.getBranch().getId())) {
            return "redirect:/recruiter/index";
        }

        app.setStatus(ApplicationStatus.REJECTED);
        applicationRepository.save(app);

        return "redirect:/recruiter/job/" + app.getJob().getId() + "/applications";
    }

    @PostMapping("/application/{id}/remove")
    public String removeApplication(@PathVariable Integer id, HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null || !user.getRole().name().equals("RECRUITER")) {
            return "redirect:/login";
        }

        Application app = applicationRepository.findById(id).orElse(null);
        if (app == null) return "redirect:/recruiter/index";

        Recruiter recruiter = recruiterRepository.findByUserId(user.getId());
        if (!app.getJob().getBranch().getId().equals(recruiter.getBranch().getId())) {
            return "redirect:/recruiter/index";
        }

        Integer jobId = app.getJob().getId();
        applicationRepository.delete(app);

        return "redirect:/recruiter/job/" + jobId + "/applications";
    }


}
