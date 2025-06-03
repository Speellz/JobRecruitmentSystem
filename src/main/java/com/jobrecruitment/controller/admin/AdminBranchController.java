package com.jobrecruitment.controller.admin;

import com.jobrecruitment.model.recruiter.Recruiter;
import com.jobrecruitment.model.company.Branch;
import com.jobrecruitment.service.company.BranchService;
import com.jobrecruitment.service.company.CompanyService;
import com.jobrecruitment.service.recruiter.RecruiterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/branches")
public class AdminBranchController {

    @Autowired
    private BranchService branchService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private RecruiterService recruiterService;

    @GetMapping("/{branchId}")
    public String viewBranchDetail(@PathVariable("branchId") Integer branchId, Model model) {
        Branch branch = branchService.getBranchById(branchId);
        if (branch == null) {
            return "redirect:/admin/companies";
        }

        List<Recruiter> recruiters = recruiterService.getRecruitersByBranch(branch);

        model.addAttribute("branch", branch);
        model.addAttribute("recruiters", recruiters);

        return "admin/branch-detail";
    }

    @GetMapping("/{branchId}/assign-manager")
    public String showAssignManagerForm(@PathVariable Integer branchId, Model model) {
        Branch branch = branchService.getBranchById(branchId);
        List<Recruiter> recruiters = recruiterService.findByBranchId(branchId);
        model.addAttribute("branch", branch);
        model.addAttribute("recruiters", recruiters);
        return "admin/assign_manager";
    }

    @PostMapping("/{branchId}/assign-manager")
    public String assignManager(
            @PathVariable Integer branchId,
            @RequestParam Integer managerRecruiterId
    ) {
        branchService.assignManager(branchId, managerRecruiterId);
        return "redirect:/admin/branches/" + branchId + "?managerAssigned=true";
    }
}
