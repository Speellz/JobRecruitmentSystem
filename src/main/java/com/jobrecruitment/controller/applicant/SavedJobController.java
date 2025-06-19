package com.jobrecruitment.controller.applicant;

import com.jobrecruitment.model.User;
import com.jobrecruitment.model.applicant.Application;
import com.jobrecruitment.model.applicant.SavedJob;
import com.jobrecruitment.model.recruiter.JobPosting;
import com.jobrecruitment.repository.UserRepository;
import com.jobrecruitment.repository.applicant.ApplicationRepository;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/applicant/saved")
@RequiredArgsConstructor
public class SavedJobController {

    private final SavedJobRepository savedJobRepository;
    private final JobPostingRepository jobPostingRepository;
    private final UserRepository userRepository;
    private final ApplicationRepository applicationRepository;


    @GetMapping("/jobs")
    public String viewSavedJobs(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null || !user.getRole().name().equals("APPLICANT")) {
            return "redirect:/auth/login";
        }

        List<SavedJob> savedJobs = savedJobRepository.findByApplicantId(user.getId());
        List<JobPosting> jobList = savedJobs.stream()
                .map(SavedJob::getJob)
                .toList();

        Map<Integer, Boolean> appliedMap = new HashMap<>();
        List<Application> applications = applicationRepository.findByApplicantId(user.getId().intValue());
        for (Application app : applications) {
            appliedMap.put(app.getJob().getId(), true);
        }

        model.addAttribute("jobList", jobList);
        model.addAttribute("appliedMap", appliedMap);
        model.addAttribute("savedMap", jobList.stream().collect(Collectors.toMap(JobPosting::getId, j -> true)));

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
