package com.jobrecruitment.controller.company;

import com.jobrecruitment.model.User;
import com.jobrecruitment.model.company.Branch;
import com.jobrecruitment.model.recruiter.Recruiter;
import com.jobrecruitment.service.company.BranchService;
import com.jobrecruitment.service.common.UserService;
import com.jobrecruitment.service.recruiter.RecruiterService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/company/branches")
@RequiredArgsConstructor
public class BranchController {

    private final BranchService branchService;

    private final UserService userService;

    private final RecruiterService recruiterService;

    @GetMapping
    public String listBranches(Model model, Principal principal) {
        if (principal == null) return "redirect:/auth/login";

        User user = userService.findByEmail(principal.getName());
        if (user == null || user.getCompany() == null) return "redirect:/";

        List<Branch> branches = branchService.getBranchesByCompanyId(user.getCompany().getId());
        model.addAttribute("branches", branches);
        return "company/branches";
    }

    @PostMapping("/add")
    public String addBranch(@RequestParam String name,
                            @RequestParam String location,
                            Principal principal) {
        if (principal == null) return "redirect:/auth/login";

        User user = userService.findByEmail(principal.getName());
        if (user == null || user.getCompany() == null) return "redirect:/";

        Branch branch = new Branch();
        branch.setName(name);
        branch.setLocation(location);
        branch.setCompany(user.getCompany());
        branch.setCreatedAt(LocalDateTime.now());

        branchService.addBranch(branch);
        return "redirect:/company/branches";
    }

    @PostMapping("/delete/{id}")
    public String deleteBranch(@PathVariable Integer id) {
        branchService.deleteBranch(id);
        return "redirect:/company/branches";
    }

    @GetMapping("/edit/{id}")
    public String editBranch(@PathVariable Integer id, Model model) {
        Branch branch = branchService.getBranchById(id);
        model.addAttribute("branch", branch);
        return "company/edit-branch";
    }

    @PostMapping("/update")
    public String updateBranch(@ModelAttribute Branch branch) {
        branchService.updateBranch(branch);
        return "redirect:/company/branches";
    }

    @GetMapping("/detail/{id}")
    public String viewBranchDetail(@PathVariable Integer id, Model model) {
        Branch branch = branchService.getBranchById(id);
        if (branch == null) return "redirect:/company/branches";

        List<Recruiter> recruiters = recruiterService.findByBranchId(id);
        String formattedCreatedAt = branch.getCreatedAt().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));

        model.addAttribute("branch", branch);
        model.addAttribute("recruiters", recruiters);
        model.addAttribute("formattedCreatedAt", formattedCreatedAt);
        return "company/branch-detail";
    }

    @PostMapping("/update-manager/{id}")
    public String updateManager(@PathVariable("id") Integer branchId,
                                @RequestParam("managerId") Integer managerId) {

        Branch branch = branchService.getBranchById(branchId);
        Recruiter manager = recruiterService.getRecruiterById(managerId);

        if (branch != null && manager != null) {
            branch.setManager(manager.getUser());
            branchService.updateBranch(branch);
        }

        return "redirect:/company/managers";
    }
}
