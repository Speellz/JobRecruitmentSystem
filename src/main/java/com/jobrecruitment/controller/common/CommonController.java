package com.jobrecruitment.controller.common;

import com.jobrecruitment.model.User;
import com.jobrecruitment.model.applicant.Certification;
import com.jobrecruitment.model.applicant.Education;
import com.jobrecruitment.model.applicant.EmploymentHistory;
import com.jobrecruitment.model.applicant.UserSkill;
import com.jobrecruitment.model.recruiter.Recruiter;
import com.jobrecruitment.model.recruiter.RecruiterAnalytics;
import com.jobrecruitment.repository.UserRepository;
import com.jobrecruitment.repository.applicant.CertificationRepository;
import com.jobrecruitment.repository.applicant.EducationRepository;
import com.jobrecruitment.repository.applicant.EmploymentHistoryRepository;
import com.jobrecruitment.repository.applicant.UserSkillRepository;
import com.jobrecruitment.repository.recruiter.RecruiterAnalyticsRepository;
import com.jobrecruitment.repository.recruiter.RecruiterRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CommonController {

    private final EducationRepository educationRepository;
    private final EmploymentHistoryRepository employmentHistoryRepository;
    private final CertificationRepository certificationRepository;
    private final UserSkillRepository userSkillRepository;
    private final RecruiterRepository recruiterRepository;
    private final RecruiterAnalyticsRepository recruiterAnalyticsRepository;

    @PostMapping("/set-role")
    public String setRoleType(@RequestParam String roleType, HttpSession session) {
        if ("business".equals(roleType)) {
            session.setAttribute("roleType", "business");
        } else {
            session.removeAttribute("roleType");
        }
        return "redirect:/";
    }

    @GetMapping("/profile")
    public String userProfile(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) {
            return "redirect:/auth/login";
        }

        model.addAttribute("user", user);

        if (user.getRole().name().equals("APPLICANT")) {
            List<Education> educationList = educationRepository.findByUserId(user.getId());
            List<EmploymentHistory> employmentList = employmentHistoryRepository.findByUserId(user.getId());
            List<Certification> certificationList = certificationRepository.findByUserId(user.getId());
            List<UserSkill> skillList = userSkillRepository.findByUserId(user.getId());

            model.addAttribute("educationList", educationList);
            model.addAttribute("employmentList", employmentList);
            model.addAttribute("certificationList", certificationList);
            model.addAttribute("skillList", skillList);
        }

        if (user.getRole().name().equals("RECRUITER")) {
            Recruiter recruiter = recruiterRepository.findByUserId(user.getId());
            if (recruiter != null) {
                RecruiterAnalytics analytics = recruiterAnalyticsRepository.findByRecruiterId(recruiter.getId());
                model.addAttribute("recruiter", recruiter);
                model.addAttribute("analytics", analytics);
            }
        }

        return "common/profile";
    }
}