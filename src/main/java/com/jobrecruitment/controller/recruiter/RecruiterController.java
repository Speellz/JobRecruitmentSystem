package com.jobrecruitment.controller.recruiter;

import com.jobrecruitment.model.applicant.Certification;
import com.jobrecruitment.model.applicant.Education;
import com.jobrecruitment.model.applicant.EmploymentHistory;
import com.jobrecruitment.model.applicant.UserSkill;
import com.jobrecruitment.model.recruiter.Recruiter;
import com.jobrecruitment.model.User;
import com.jobrecruitment.service.company.BranchService;
import com.jobrecruitment.service.recruiter.RecruiterService;
import com.jobrecruitment.service.common.UserService;
import com.jobrecruitment.repository.UserRepository;
import com.jobrecruitment.repository.applicant.CertificationRepository;
import com.jobrecruitment.repository.applicant.EducationRepository;
import com.jobrecruitment.repository.applicant.EmploymentHistoryRepository;
import com.jobrecruitment.repository.applicant.UserSkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/recruiter")
@RequiredArgsConstructor
public class RecruiterController {

    private final RecruiterService recruiterService;
    private final BranchService branchService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final EducationRepository educationRepository;
    private final EmploymentHistoryRepository employmentHistoryRepository;
    private final CertificationRepository certificationRepository;
    private final UserSkillRepository userSkillRepository;

    @GetMapping("/add")
    public String showRecruiterForm(Model model, Principal principal, RedirectAttributes redirectAttributes) {
        if (principal == null) {
            redirectAttributes.addFlashAttribute("error", "You must be logged in.");
            return "redirect:/auth/login";
        }

        User owner = userService.findByEmail(principal.getName());

        if (owner == null || owner.getCompany() == null) {
            redirectAttributes.addFlashAttribute("error", "Unauthorized access.");
            return "redirect:/";
        }

        model.addAttribute("recruiter", new Recruiter());
        model.addAttribute("branches", branchService.getBranchesByCompanyId(owner.getCompany().getId()));
        return "add-recruiter";
    }

    @PostMapping("/add")
    public String addRecruiter(@ModelAttribute Recruiter recruiter,
                               Principal principal,
                               RedirectAttributes redirectAttributes,
                               Model model) {

        if (principal == null) {
            redirectAttributes.addFlashAttribute("error", "Login required.");
            return "redirect:/auth/login";
        }

        User owner = userService.findByEmail(principal.getName());

        if (owner == null || owner.getRole() != User.Role.COMPANY) {
            redirectAttributes.addFlashAttribute("error", "Unauthorized access.");
            return "redirect:/";
        }

        recruiter.setCompany(owner.getCompany());

        boolean success = recruiterService.addRecruiter(recruiter);

        if (!success) {
            redirectAttributes.addFlashAttribute("error", "Recruiter already exists or invalid details.");
            return "redirect:/recruiter/add";
        }

        redirectAttributes.addFlashAttribute("success", "Recruiter successfully added.");
        return "redirect:/company/dashboard";
    }
    @GetMapping("/application/{applicantId}/profile")
    public String viewApplicantProfile(@PathVariable Integer applicantId, Model model) {
        Long userId = applicantId.longValue();
        User applicant = userRepository.findById(userId).orElse(null);
        if (applicant == null) return "redirect:/";

        model.addAttribute("user", applicant);

        if (applicant.getRole().name().equals("APPLICANT")) {
            List<Education> educationList = educationRepository.findByUserId(userId);
            List<EmploymentHistory> employmentList = employmentHistoryRepository.findByUserId(userId);
            List<Certification> certificationList = certificationRepository.findByUserId(userId);
            List<UserSkill> skillList = userSkillRepository.findByUserId(userId);

            model.addAttribute("educationList", educationList);
            model.addAttribute("employmentList", employmentList);
            model.addAttribute("certificationList", certificationList);
            model.addAttribute("skillList", skillList);
        }

        return "recruiter/applicant-detail";
    }
}
