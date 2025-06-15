package com.jobrecruitment.controller.applicant;

import com.jobrecruitment.model.User;
import com.jobrecruitment.model.applicant.ApplicationStatus;
import com.jobrecruitment.model.recruiter.JobPosting;
import com.jobrecruitment.model.applicant.Application;
import com.jobrecruitment.repository.UserRepository;
import com.jobrecruitment.repository.recruiter.JobPostingRepository;
import com.jobrecruitment.repository.applicant.ApplicationRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class JobApplicationController {

    private final JobPostingRepository jobPostingRepository;
    private final UserRepository userRepository;
    private final ApplicationRepository ApplicationRepository;

    @GetMapping("/job/{id}/apply")
    public String showApplyForm(@PathVariable Integer id, Model model, HttpSession session) {
        Optional<JobPosting> jobOpt = jobPostingRepository.findById(id);
        if (jobOpt.isEmpty()) return "redirect:/?error=Job not found";

        User applicant = (User) session.getAttribute("loggedUser");
        if (applicant != null && applicant.getRole().name().equals("APPLICANT")) {
            boolean alreadyApplied = ApplicationRepository.existsByJobIdAndApplicantId(id, applicant.getId());
            if (alreadyApplied) {
                return "redirect:/?error=You have already applied for this job";
            }
        }

        model.addAttribute("job", jobOpt.get());
        return "applicant/apply";
    }

    @PostMapping("/job/{id}/apply")
    public String applyToJob(@PathVariable Integer id,
                             @RequestParam("cvFile") MultipartFile cvFile,
                             @RequestParam("coverLetter") String coverLetter,
                             HttpSession session) {

        User applicant = (User) session.getAttribute("loggedUser");
        if (applicant == null || !applicant.getRole().name().equals("APPLICANT")) {
            return "redirect:/login?error=Unauthorized";
        }

        Optional<JobPosting> jobOpt = jobPostingRepository.findById(id);
        if (jobOpt.isEmpty()) return "redirect:/?error=Job not found";
        JobPosting job = jobOpt.get();

        boolean alreadyApplied = ApplicationRepository.existsByJobIdAndApplicantId(id, applicant.getId());
        if (alreadyApplied) {
            return "redirect:/?error=You have already applied for this job";
        }

        String uploadDir = "C:/cv-uploads/";
        File dir = new File(uploadDir);
        if (!dir.exists()) dir.mkdirs();

        String fileName = UUID.randomUUID() + "_" + cvFile.getOriginalFilename();
        File savedFile = new File(uploadDir + fileName);
        try {
            cvFile.transferTo(savedFile);
        } catch (IOException e) {
            return "redirect:/?error=Failed to upload CV";
        }

        Application app = new Application();
        app.setJob(job);
        app.setApplicant(applicant);
        app.setCvUrl("/uploads/cv/" + fileName);
        app.setCoverLetter(coverLetter);
        app.setStatus(ApplicationStatus.PENDING);
        app.setAppliedAt(LocalDateTime.now());

        ApplicationRepository.save(app);
        return "redirect:/?success=Application submitted";
    }
}
