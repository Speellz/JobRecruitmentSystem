package com.jobrecruitment.controller.recruiter;

import com.jobrecruitment.model.recruiter.Recruiter;
import com.jobrecruitment.model.User;
import com.jobrecruitment.service.company.BranchService;
import com.jobrecruitment.service.recruiter.RecruiterService;
import java.security.Principal;
import com.jobrecruitment.service.common.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/recruiter")
@RequiredArgsConstructor
public class RecruiterController {

    private final RecruiterService recruiterService;

    private final BranchService branchService;

    private final UserService userService;


    @GetMapping("/add")
    public String showRecruiterForm(Model model, Principal principal) {
        if (principal == null) return "redirect:/auth/login";
        User owner = userService.findByEmail(principal.getName());

        if (owner == null || owner.getCompany() == null) {
            model.addAttribute("error", "Unauthorized access.");
            return "error";
        }

        model.addAttribute("recruiter", new Recruiter());
        model.addAttribute("branches", branchService.getBranchesByCompanyId(owner.getCompany().getId()));
        return "add-recruiter";
    }


    @PostMapping("/add")
    public String addRecruiter(@ModelAttribute Recruiter recruiter,
                               Model model,
                               Principal principal) {

        if (principal == null) return "redirect:/auth/login";
        User owner = userService.findByEmail(principal.getName());

        if (owner == null || owner.getRole() != User.Role.COMPANY) {
            model.addAttribute("error", "Unauthorized access.");
            return "error";
        }

        recruiter.setCompany(owner.getCompany());

        boolean success = recruiterService.addRecruiter(recruiter);

        if (!success) {
            model.addAttribute("error", "Recruiter already exists or invalid details.");
            return "add-recruiter";
        }

        return "redirect:/company/dashboard";
    }

}

