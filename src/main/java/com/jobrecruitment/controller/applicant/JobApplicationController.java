package com.jobrecruitment.controller.applicant;

import com.jobrecruitment.model.User;
import com.jobrecruitment.model.applicant.ApplicationStatus;
import com.jobrecruitment.model.applicant.Referral;
import com.jobrecruitment.model.recruiter.InterviewFeedback;
import com.jobrecruitment.model.recruiter.InterviewSchedule;
import com.jobrecruitment.model.recruiter.JobPosting;
import com.jobrecruitment.model.applicant.Application;
import com.jobrecruitment.repository.UserRepository;
import com.jobrecruitment.repository.applicant.ReferralRepository;
import com.jobrecruitment.repository.recruiter.InterviewFeedbackRepository;
import com.jobrecruitment.repository.recruiter.InterviewScheduleRepository;
import com.jobrecruitment.repository.recruiter.JobPostingRepository;
import com.jobrecruitment.repository.applicant.ApplicationRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Controller
@RequiredArgsConstructor
public class JobApplicationController {

    private final JobPostingRepository jobPostingRepository;
    private final ApplicationRepository applicationRepository;
    private final com.jobrecruitment.repository.recruiter.InterviewScheduleRepository interviewScheduleRepository;
    private final InterviewFeedbackRepository interviewFeedbackRepository;
    private final ReferralRepository referralRepository;
    private final UserRepository userRepository;

    @GetMapping("/job/{id}/apply")
    public String showApplyForm(@PathVariable Integer id, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        Optional<JobPosting> jobOpt = jobPostingRepository.findById(id);
        if (jobOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Job not found.");
            return "redirect:/";
        }

        User applicant = (User) session.getAttribute("loggedUser");
        if (applicant != null && applicant.getRole().name().equals("APPLICANT")) {
            boolean alreadyApplied = applicationRepository.existsByJobIdAndApplicantId(id, applicant.getId());
            if (alreadyApplied) {
                redirectAttributes.addFlashAttribute("error", "You have already applied for this job.");
                return "redirect:/";
            }
        }

        model.addAttribute("job", jobOpt.get());
        return "applicant/apply";
    }

    @PostMapping("/job/{id}/apply")
    public String applyToJob(@PathVariable Integer id,
                             @RequestParam(value = "cvFile", required = false) MultipartFile cvFile,
                             @RequestParam("coverLetter") String coverLetter,
                             @RequestParam(value = "referrerEmail", required = false) String referrerEmail,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {

        User applicant = (User) session.getAttribute("loggedUser");
        if (applicant == null || !applicant.getRole().name().equals("APPLICANT")) {
            redirectAttributes.addFlashAttribute("error", "Unauthorized access.");
            return "redirect:/login";
        }

        Optional<JobPosting> jobOpt = jobPostingRepository.findById(id);
        if (jobOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Job not found.");
            return "redirect:/";
        }

        JobPosting job = jobOpt.get();
        boolean alreadyApplied = applicationRepository.existsByJobIdAndApplicantId(id, applicant.getId());
        if (alreadyApplied) {
            redirectAttributes.addFlashAttribute("error", "You have already applied for this job.");
            return "redirect:/";
        }

        boolean hasProfileCv = applicant.getCvUrl() != null && !applicant.getCvUrl().isBlank();
        boolean fileUploaded = cvFile != null && !cvFile.isEmpty();

        if (!hasProfileCv && !fileUploaded) {
            redirectAttributes.addFlashAttribute("error", "Please upload a CV or add it to your profile first.");
            return "redirect:/job/" + id + "/apply";
        }

        String finalCvUrl;

        if (hasProfileCv) {
            finalCvUrl = applicant.getCvUrl();
        } else {
            String uploadDir = "C:/cv-uploads/";
            File dir = new File(uploadDir);
            if (!dir.exists()) dir.mkdirs();

            String fileName = UUID.randomUUID() + "_" + cvFile.getOriginalFilename();
            File savedFile = new File(uploadDir + fileName);
            try {
                cvFile.transferTo(savedFile);
            } catch (IOException e) {
                redirectAttributes.addFlashAttribute("error", "Failed to upload CV.");
                return "redirect:/";
            }
            finalCvUrl = "/uploads/cv/" + fileName;
        }

        Application app = new Application();
        app.setJob(job);
        app.setApplicant(applicant);
        app.setCvUrl(finalCvUrl);
        app.setCoverLetter(coverLetter);
        app.setStatus(ApplicationStatus.PENDING);
        app.setAppliedAt(LocalDateTime.now());

        applicationRepository.save(app);

        if (referrerEmail != null && !referrerEmail.isBlank()) {
            Optional<User> referrerOpt = userRepository.findByEmail(referrerEmail.trim());
            if (referrerOpt.isPresent() && !referrerOpt.get().getId().equals(applicant.getId())) {
                Referral referral = new Referral();
                referral.setReferredUser(applicant);
                referral.setReferrerUser(referrerOpt.get());
                referral.setJob(job);
                referralRepository.save(referral);
            }
        }

        redirectAttributes.addFlashAttribute("success", "Application submitted successfully.");
        return "redirect:/";
    }


    @GetMapping("/applicant/applications")
    public String getMyApplications(HttpSession session, Model model) {
        User applicant = (User) session.getAttribute("loggedUser");
        if (applicant == null) {
            return "redirect:/auth/login";
        }

        List<Application> applications = applicationRepository.findByApplicantId(applicant.getId().intValue());

        Map<Integer, InterviewSchedule> interviewMap = new HashMap<>();
        Map<Integer, Date> interviewTimeMap = new HashMap<>();
        Map<Integer, InterviewFeedback> interviewFeedbackMap = new HashMap<>();

        for (Application app : applications) {
            Optional<InterviewSchedule> interviewOpt = interviewScheduleRepository
                    .findByApplicantIdAndJobId(app.getApplicant().getId().longValue(), app.getJob().getId().longValue());

            interviewOpt.ifPresent(interview -> {
                interviewMap.put(app.getId(), interview);
                Date convertedDate = Date.from(interview.getTime().atZone(ZoneId.systemDefault()).toInstant());
                interviewTimeMap.put(app.getId(), convertedDate);

                interviewFeedbackRepository.findByInterview(interview)
                        .ifPresent(feedback -> interviewFeedbackMap.put(app.getId(), feedback));
            });
        }

        model.addAttribute("applications", applications);
        model.addAttribute("interviewMap", interviewMap);
        model.addAttribute("interviewTimeMap", interviewTimeMap);
        model.addAttribute("interviewFeedbackMap", interviewFeedbackMap);

        return "applicant/my-applications";
    }
}