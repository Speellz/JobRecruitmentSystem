package com.jobrecruitment.controller.company;

import com.jobrecruitment.model.User;
import com.jobrecruitment.model.company.Company;
import com.jobrecruitment.model.company.CompanyReview;
import com.jobrecruitment.model.recruiter.InterviewSchedule;
import com.jobrecruitment.repository.company.CompanyRepository;
import com.jobrecruitment.repository.company.CompanyFeedbackRepository;
import com.jobrecruitment.repository.recruiter.InterviewScheduleRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.*;

@Controller
@RequestMapping("/applicant/company")
@RequiredArgsConstructor
public class CompanyFeedbackController {

    private final InterviewScheduleRepository interviewScheduleRepository;
    private final CompanyFeedbackRepository companyFeedbackRepository;
    private final CompanyRepository companyRepository;

    @GetMapping("/feedback")
    public String showFeedbackPage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null || !user.getRole().name().equals("APPLICANT")) {
            return "redirect:/auth/login";
        }

        LocalDateTime now = LocalDateTime.now();
        List<InterviewSchedule> interviews = interviewScheduleRepository
                .findByApplicant_IdAndTimeBefore(user.getId().longValue(), now);

        Map<Long, CompanyReview> reviewMap = new HashMap<>();
        for (InterviewSchedule interview : interviews) {
            Company company = interview.getJob().getCompany();
            companyFeedbackRepository.findByCompanyIdAndUserId(
                    company.getId().longValue(), user.getId().longValue()
            ).ifPresent(review -> reviewMap.put(interview.getId().longValue(), review));
        }

        model.addAttribute("interviews", interviews);
        model.addAttribute("reviewMap", reviewMap);
        return "applicant/company-feedback";
    }

    @PostMapping("/feedback/submit")
    public String submitFeedback(@RequestParam("interviewId") Long interviewId,
                                 @RequestParam("rating") int rating,
                                 @RequestParam("reviewText") String reviewText,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null || !user.getRole().name().equals("APPLICANT")) {
            return "redirect:/auth/login";
        }

        Optional<InterviewSchedule> interviewOpt = interviewScheduleRepository.findById(interviewId.intValue());
        if (interviewOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Interview not found.");
            return "redirect:/applicant/company/feedback";
        }

        Company company = interviewOpt.get().getJob().getCompany();
        if (companyFeedbackRepository.existsByCompanyIdAndUserId(
                company.getId().longValue(), user.getId().longValue()
        )) {
            redirectAttributes.addFlashAttribute("error", "You already reviewed this company.");
            return "redirect:/applicant/company/feedback";
        }

        CompanyReview review = new CompanyReview();
        review.setCompany(company);
        review.setUser(user);
        review.setRating(rating);
        review.setReviewText(reviewText);
        review.setCreatedAt(LocalDateTime.now());

        companyFeedbackRepository.save(review);
        redirectAttributes.addFlashAttribute("success", "Your review has been submitted.");
        return "redirect:/applicant/company/feedback";
    }
}
