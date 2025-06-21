package com.jobrecruitment.controller.recruiter;

import com.jobrecruitment.model.User;
import com.jobrecruitment.model.applicant.Application;
import com.jobrecruitment.model.applicant.ApplicationStatus;
import com.jobrecruitment.model.recruiter.JobPosting;
import com.jobrecruitment.model.recruiter.Recruiter;
import com.jobrecruitment.model.recruiter.RecruiterAnalytics;
import com.jobrecruitment.repository.applicant.ApplicationRepository;
import com.jobrecruitment.repository.applicant.ReferralRepository;
import com.jobrecruitment.repository.recruiter.InterviewScheduleRepository;
import com.jobrecruitment.repository.recruiter.JobPostingRepository;
import com.jobrecruitment.repository.recruiter.RecruiterAnalyticsRepository;
import com.jobrecruitment.repository.recruiter.RecruiterRepository;
import com.jobrecruitment.service.common.UserNotificationService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/recruiter")
public class RecruiterApplicationController {

    private final JobPostingRepository jobPostingRepository;
    private final ApplicationRepository applicationRepository;
    private final RecruiterRepository recruiterRepository;
    private final InterviewScheduleRepository interviewScheduleRepository;
    private final ReferralRepository referralRepository;
    private final UserNotificationService userNotificationService;
    private final RecruiterAnalyticsRepository recruiterAnalyticsRepository;

    @GetMapping("/job/{jobId}/applications")
    public String viewApplications(@PathVariable Integer jobId, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null || !(user.getRole().name().equals("RECRUITER") || user.getRole().name().equals("ADMIN"))) {
            return "redirect:/auth/login";
        }

        JobPosting job = jobPostingRepository.findById(jobId).orElse(null);
        if (job == null) {
            redirectAttributes.addFlashAttribute("error", "Job not found.");
            return "redirect:/";
        }

        if (user.getRole().name().equals("RECRUITER")) {
            Recruiter recruiter = recruiterRepository.findByUserId(user.getId());
            if (recruiter == null || !job.getBranch().getId().equals(recruiter.getBranch().getId())) {
                redirectAttributes.addFlashAttribute("error", "Unauthorized access.");
                return "redirect:/recruiter/index";
            }
        }


        List<Application> applications = applicationRepository.findByJobId(jobId);

        List<Map<String, Object>> enrichedList = applications.stream().map(app -> {
            Map<String, Object> map = new HashMap<>();
            map.put("application", app);
            map.put("applicant", app.getApplicant());
            map.put("status", app.getStatus());
            map.put("cvUrl", app.getCvUrl());
            map.put("coverLetter", app.getCoverLetter());
            map.put("id", app.getId());

            interviewScheduleRepository
                    .findByApplicantIdAndJobId(app.getApplicant().getId().longValue(), app.getJob().getId().longValue())
                    .ifPresent(interview -> {
                        map.put("interviewTime", interview.getTime());
                        map.put("interviewId", interview.getId());
                    });

            referralRepository.findByReferredUserAndJob(app.getApplicant(), app.getJob())
                    .ifPresent(referral -> map.put("referrer", referral.getReferrerUser()));

            return map;
        }).toList();

        model.addAttribute("job", job);
        model.addAttribute("applications", enrichedList);
        return "recruiter/applications";
    }



    @PostMapping("/application/{id}/approve")
    public String approveApplication(@PathVariable Integer id, HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null || !user.getRole().name().equals("RECRUITER")) {
            return "redirect:/login";
        }

        Application app = applicationRepository.findById(id).orElse(null);
        if (app == null) {
            redirectAttributes.addFlashAttribute("error", "Application not found.");
            return "redirect:/recruiter/index";
        }

        Recruiter recruiter = recruiterRepository.findByUserId(user.getId());
        if (!app.getJob().getBranch().getId().equals(recruiter.getBranch().getId())) {
            redirectAttributes.addFlashAttribute("error", "Unauthorized action.");
            return "redirect:/recruiter/index";
        }

        app.setStatus(ApplicationStatus.APPROVED);
        applicationRepository.save(app);

        userNotificationService.sendNotification(
                app.getApplicant(),
                "Your application for \"" + app.getJob().getTitle() + "\" is pending interview scheduling.",
                "/applicant/applications"
        );

        redirectAttributes.addFlashAttribute("success", "Application approved pending interview scheduling.");
        return "redirect:/recruiter/interview/schedule/" + app.getId();
    }

    @PostMapping("/application/{id}/reject")
    public String rejectApplication(@PathVariable Integer id, HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null || !user.getRole().name().equals("RECRUITER")) {
            return "redirect:/login";
        }

        Application app = applicationRepository.findById(id).orElse(null);
        if (app == null) {
            redirectAttributes.addFlashAttribute("error", "Application not found.");
            return "redirect:/recruiter/index";
        }

        Recruiter recruiter = recruiterRepository.findByUserId(user.getId());
        if (!app.getJob().getBranch().getId().equals(recruiter.getBranch().getId())) {
            redirectAttributes.addFlashAttribute("error", "Unauthorized action.");
            return "redirect:/recruiter/index";
        }

        app.setStatus(ApplicationStatus.REJECTED);
        applicationRepository.save(app);

        userNotificationService.sendNotification(
                app.getApplicant(),
                "Your application for \"" + app.getJob().getTitle() + "\" has been rejected.",
                "/applicant/applications"
        );

        redirectAttributes.addFlashAttribute("success", "Application rejected.");
        return "redirect:/recruiter/job/" + app.getJob().getId() + "/applications";
    }

    @PostMapping("/application/{id}/remove")
    public String removeApplication(@PathVariable Integer id, HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null || !user.getRole().name().equals("RECRUITER")) {
            return "redirect:/login";
        }

        Application app = applicationRepository.findById(id).orElse(null);
        if (app == null) {
            redirectAttributes.addFlashAttribute("error", "Application not found.");
            return "redirect:/recruiter/index";
        }

        Recruiter recruiter = recruiterRepository.findByUserId(user.getId());
        if (!app.getJob().getBranch().getId().equals(recruiter.getBranch().getId())) {
            redirectAttributes.addFlashAttribute("error", "Unauthorized action.");
            return "redirect:/recruiter/index";
        }

        Integer jobId = app.getJob().getId();

        userNotificationService.sendNotification(
                app.getApplicant(),
                "Your application for \"" + app.getJob().getTitle() + "\" has been removed by the recruiter.",
                "/applicant/applications"
        );

        applicationRepository.delete(app);

        redirectAttributes.addFlashAttribute("success", "Application removed.");
        return "redirect:/recruiter/job/" + jobId + "/applications";
    }
}
