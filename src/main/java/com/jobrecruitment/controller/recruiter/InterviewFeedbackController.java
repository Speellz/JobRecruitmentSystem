package com.jobrecruitment.controller.recruiter;

import com.jobrecruitment.model.User;
import com.jobrecruitment.model.applicant.Application;
import com.jobrecruitment.model.applicant.ApplicationStatus;
import com.jobrecruitment.model.recruiter.InterviewFeedback;
import com.jobrecruitment.model.recruiter.InterviewResult;
import com.jobrecruitment.model.recruiter.InterviewSchedule;
import com.jobrecruitment.model.recruiter.RecruiterAnalytics;
import com.jobrecruitment.repository.applicant.ApplicationRepository;
import com.jobrecruitment.repository.recruiter.InterviewFeedbackRepository;
import com.jobrecruitment.repository.recruiter.InterviewScheduleRepository;
import com.jobrecruitment.repository.recruiter.RecruiterAnalyticsRepository;
import com.jobrecruitment.service.common.UserNotificationService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@Controller
@RequestMapping("/recruiter/interview")
@RequiredArgsConstructor
public class InterviewFeedbackController {

    private final InterviewScheduleRepository interviewScheduleRepository;
    private final InterviewFeedbackRepository interviewFeedbackRepository;
    private final UserNotificationService userNotificationService;
    private final ApplicationRepository applicationRepository;
    private final RecruiterAnalyticsRepository recruiterAnalyticsRepository;

    @GetMapping("/feedback")
    public String showFeedbackPage(HttpSession session, Model model) {
        User recruiter = (User) session.getAttribute("loggedUser");
        if (recruiter == null || !recruiter.getRole().name().equals("RECRUITER")) {
            return "redirect:/login";
        }

        LocalDateTime now = LocalDateTime.now();

        List<InterviewSchedule> interviewList = interviewScheduleRepository
                .findByRecruiter_IdAndTimeBefore(recruiter.getId(), now);

        Map<Integer, InterviewFeedback> feedbackMap = new HashMap<>();
        for (InterviewSchedule interview : interviewList) {
            interviewFeedbackRepository.findByInterview(interview).ifPresent(fb ->
                    feedbackMap.put(interview.getId(), fb));
        }

        model.addAttribute("interviews", interviewList);
        model.addAttribute("feedbackMap", feedbackMap);

        return "recruiter/interview-feedback";
    }

    @GetMapping("/feedback/edit/{interviewId}")
    public String showEditForm(@PathVariable Integer interviewId, HttpSession session, Model model) {
        User recruiter = (User) session.getAttribute("loggedUser");
        if (recruiter == null || !recruiter.getRole().name().equals("RECRUITER")) {
            return "redirect:/login";
        }

        Optional<InterviewSchedule> interviewOpt = interviewScheduleRepository.findById(interviewId);
        if (interviewOpt.isEmpty()) {
            return "redirect:/recruiter/interview/feedback?error=notfound";
        }

        InterviewSchedule interview = interviewOpt.get();
        Optional<InterviewFeedback> feedbackOpt = interviewFeedbackRepository.findByInterview(interview);
        if (feedbackOpt.isEmpty()) {
            return "redirect:/recruiter/interview/feedback?error=nofeedback";
        }

        model.addAttribute("interview", interview);
        model.addAttribute("feedback", feedbackOpt.get());
        return "recruiter/edit-feedback";
    }

    @PostMapping("/feedback/submit")
    public String submitFeedback(@RequestParam("interviewId") Integer interviewId,
                                 @RequestParam("score") Integer score,
                                 @RequestParam("result") InterviewResult result,
                                 @RequestParam("feedback") String feedbackText,
                                 HttpSession session) {

        User recruiter = (User) session.getAttribute("loggedUser");
        if (recruiter == null || !recruiter.getRole().name().equals("RECRUITER")) {
            return "redirect:/login";
        }

        Optional<InterviewSchedule> interviewOpt = interviewScheduleRepository.findById(interviewId);
        if (interviewOpt.isEmpty()) {
            return "redirect:/recruiter/interview/feedback?error=notfound";
        }

        InterviewSchedule interview = interviewOpt.get();

        if (interviewFeedbackRepository.existsByInterview(interview)) {
            return "redirect:/recruiter/interview/feedback?error=already_submitted";
        }

        InterviewFeedback feedback = new InterviewFeedback();
        feedback.setInterview(interview);
        feedback.setFeedback(feedbackText);
        feedback.setScore(score);
        feedback.setResult(result);
        feedback.setSubmittedAt(LocalDateTime.now());
        feedback.setSubmittedBy(recruiter);

        interviewFeedbackRepository.save(feedback);

        if (result == InterviewResult.APPROVED) {
            Application application = applicationRepository.findByApplicantIdAndJobId(
                    interview.getApplicant().getId().longValue(),
                    interview.getJob().getId().longValue()
            );
            if (application != null) {
                application.setStatus(ApplicationStatus.APPROVED);
                applicationRepository.save(application);

                RecruiterAnalytics analytics = recruiterAnalyticsRepository.findByRecruiterId(recruiter.getId().longValue());
                if (analytics != null) {
                    analytics.setTotalHires(analytics.getTotalHires() + 1);
                    recruiterAnalyticsRepository.save(analytics);
                }
            }
        }

        userNotificationService.sendNotification(
                interview.getApplicant(),
                "Your interview result has been submitted. Please check your application.",
                "/applicant/applications"
        );

        return "redirect:/recruiter/interview/feedback?success=true";
    }

    @PostMapping("/feedback/update")
    public String updateFeedback(@RequestParam("interviewId") Integer interviewId,
                                 @RequestParam("score") Integer score,
                                 @RequestParam("result") InterviewResult result,
                                 @RequestParam("feedback") String feedbackText,
                                 HttpSession session) {

        User recruiter = (User) session.getAttribute("loggedUser");
        if (recruiter == null || !recruiter.getRole().name().equals("RECRUITER")) {
            return "redirect:/login";
        }

        Optional<InterviewSchedule> interviewOpt = interviewScheduleRepository.findById(interviewId);
        if (interviewOpt.isEmpty()) {
            return "redirect:/recruiter/interview/feedback?error=notfound";
        }

        InterviewSchedule interview = interviewOpt.get();
        Optional<InterviewFeedback> feedbackOpt = interviewFeedbackRepository.findByInterview(interview);
        if (feedbackOpt.isEmpty()) {
            return "redirect:/recruiter/interview/feedback?error=nofeedback";
        }

        InterviewFeedback feedback = feedbackOpt.get();
        feedback.setScore(score);
        feedback.setResult(result);
        feedback.setFeedback(feedbackText);
        feedback.setSubmittedAt(LocalDateTime.now());

        interviewFeedbackRepository.save(feedback);

        if (result == InterviewResult.APPROVED) {
            Application application = applicationRepository.findByApplicantIdAndJobId(
                    interview.getApplicant().getId().longValue(),
                    interview.getJob().getId().longValue()
            );
            if (application != null) {
                application.setStatus(ApplicationStatus.APPROVED);
                applicationRepository.save(application);

                RecruiterAnalytics analytics = recruiterAnalyticsRepository.findByRecruiterId(recruiter.getId().longValue());
                if (analytics != null) {
                    analytics.setTotalHires(analytics.getTotalHires() + 1);
                    recruiterAnalyticsRepository.save(analytics);
                }
            }
        }

        userNotificationService.sendNotification(
                interview.getApplicant(),
                "Your interview feedback has been updated.",
                "/applicant/applications"
        );

        return "redirect:/recruiter/interview/feedback?success=updated";
    }
}
