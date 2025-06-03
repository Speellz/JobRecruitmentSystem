package com.jobrecruitment.controller.recruiter;

import com.jobrecruitment.model.recruiter.Recruiter;
import com.jobrecruitment.model.User;
import com.jobrecruitment.service.company.BranchService;
import com.jobrecruitment.service.recruiter.RecruiterService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/recruiter")
public class RecruiterController {

    @Autowired
    private RecruiterService recruiterService;

    @Autowired
    private BranchService branchService;

    @GetMapping("/add")
    public String showRecruiterForm(Model model, HttpSession session) {
        User owner = (User) session.getAttribute("loggedInUser");

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
                               HttpSession session) {

        User owner = (User) session.getAttribute("loggedInUser");

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

