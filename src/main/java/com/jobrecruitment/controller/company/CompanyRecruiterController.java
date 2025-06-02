package com.jobrecruitment.controller.company;

import com.jobrecruitment.model.Recruiter;
import com.jobrecruitment.model.User;
import com.jobrecruitment.model.company.Branch;
import com.jobrecruitment.service.company.BranchService;
import com.jobrecruitment.service.common.UserService;
import com.jobrecruitment.service.recruiter.RecruiterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/company/recruiters")
public class CompanyRecruiterController {

    @Autowired
    private RecruiterService recruiterService;

    @Autowired
    private BranchService branchService;

    @Autowired
    private UserService userService;

    @GetMapping
    public String listRecruiters(Model model, Principal principal) {
        User user = userService.findByEmail(principal.getName());
        List<Recruiter> recruiters = recruiterService.findByCompanyId(user.getCompany().getId());
        model.addAttribute("recruiters", recruiters);
        return "company/recruiters";
    }

    @GetMapping("/add")
    public String showAddRecruiterForm(Model model, Principal principal) {
        User user = userService.findByEmail(principal.getName());
        List<Branch> branches = branchService.getBranchesByCompanyId(user.getCompany().getId());
        model.addAttribute("branches", branches);
        model.addAttribute("recruiter", new Recruiter());
        return "company/add-recruiter";
    }

    @PostMapping("/add")
    public String addRecruiter(@ModelAttribute Recruiter recruiter, @RequestParam Integer branchId, Model model, Principal principal) {
        Branch branch = branchService.getBranchById(branchId);
        recruiter.setBranch(branch);
        recruiter.setCompany(branch.getCompany());
        boolean added = recruiterService.addRecruiter(recruiter);
        if (!added) {
            User user = userService.findByEmail(principal.getName());
            List<Branch> branches = branchService.getBranchesByCompanyId(user.getCompany().getId());
            model.addAttribute("branches", branches);
            model.addAttribute("recruiter", recruiter);
            model.addAttribute("error", "This email address is already used by another user.");
            return "company/add-recruiter";
        }
        return "redirect:/company/recruiters?success";
    }

    @PostMapping("/delete/{id}")
    public String deleteRecruiter(@PathVariable("id") Integer id) {
        recruiterService.deleteRecruiter(id);
        return "redirect:/company/recruiters?deleted";
    }
}
