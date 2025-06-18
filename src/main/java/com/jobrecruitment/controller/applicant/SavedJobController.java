package com.jobrecruitment.controller.applicant;

import com.jobrecruitment.model.User;
import com.jobrecruitment.model.applicant.SavedJob;
import com.jobrecruitment.model.recruiter.JobPosting;
import com.jobrecruitment.repository.UserRepository;
import com.jobrecruitment.repository.applicant.SavedJobRepository;
import com.jobrecruitment.repository.recruiter.JobPostingRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/applicant/saved")
@RequiredArgsConstructor
public class SavedJobController {

    private final SavedJobRepository savedJobRepository;
    private final JobPostingRepository jobPostingRepository;
    private final UserRepository userRepository;

    @GetMapping
    public String viewSavedJobs(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null || !user.getRole().name().equals("APPLICANT")) {
            return "redirect:/login";
        }

        List<SavedJob> savedJobs = savedJobRepository.findByApplicantId(user.getId());
        model.addAttribute("savedJobs", savedJobs);
        return "applicant/saved-jobs";
    }

    @PostMapping("/add/{jobId}")
    public String saveJob(@PathVariable Integer jobId,
                          HttpSession session,
                          RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null || !user.getRole().name().equals("APPLICANT")) {
            redirectAttributes.addFlashAttribute("error", "Unauthorized");
            return "redirect:/login";
        }

        JobPosting job = jobPostingRepository.findById(jobId).orElse(null);
        if (job == null) {
            redirectAttributes.addFlashAttribute("error", "Job not found.");
            return "redirect:/";
        }

        boolean alreadySaved = savedJobRepository.findByApplicantAndJob(user, job).isPresent();
        if (alreadySaved) {
            redirectAttributes.addFlashAttribute("error", "Already saved.");
            return "redirect:/";
        }

        SavedJob savedJob = new SavedJob();
        savedJob.setApplicant(user);
        savedJob.setJob(job);
        savedJob.setSavedAt(LocalDateTime.now());
        savedJobRepository.save(savedJob);

        redirectAttributes.addFlashAttribute("success", "Job saved.");
        return "redirect:/";
    }

    @Transactional
    @PostMapping("/remove/{jobId}")
    public String unsaveJob(@PathVariable Integer jobId,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null || !user.getRole().name().equals("APPLICANT")) {
            redirectAttributes.addFlashAttribute("error", "Unauthorized");
            return "redirect:/login";
        }

        JobPosting job = jobPostingRepository.findById(jobId).orElse(null);
        if (job == null) {
            redirectAttributes.addFlashAttribute("error", "Job not found.");
            return "redirect:/";
        }

        savedJobRepository.deleteByApplicantAndJob(user, job);
        redirectAttributes.addFlashAttribute("success", "Job removed from saved.");
        return "redirect:/";
    }
}
