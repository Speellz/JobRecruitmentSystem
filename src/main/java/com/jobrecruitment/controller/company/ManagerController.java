package com.jobrecruitment.controller.company;

import com.jobrecruitment.model.recruiter.Recruiter;
import com.jobrecruitment.model.User;
import com.jobrecruitment.model.company.Branch;
import com.jobrecruitment.service.company.BranchService;
import com.jobrecruitment.service.common.UserService;
import com.jobrecruitment.service.recruiter.RecruiterService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/company")
@RequiredArgsConstructor
public class ManagerController {

    private final BranchService branchService;

    private final UserService userService;

    private final RecruiterService recruiterService;


    @GetMapping("/assign-manager/{id}")
    public String showAssignManagerForm(@PathVariable Integer id, Model model, Principal principal) {
        Branch branch = branchService.getBranchById(id);
        if (branch == null || principal == null) return "redirect:/company/branches";

        User user = userService.findByEmail(principal.getName());
        if (user == null || user.getCompany() == null) return "redirect:/";

        List<Recruiter> recruiters = recruiterService.findByBranchId(id);
        model.addAttribute("branch", branch);
        model.addAttribute("recruiters", recruiters);
        return "company/assign-manager";
    }

    @PostMapping("/assign-manager/{id}")
    public String assignManager(@PathVariable Integer id, @RequestParam Integer managerId) {
        branchService.assignManager(id, managerId);
        return "redirect:/company/branches";
    }

    @GetMapping("/managers")
    public String listManagers(Model model, Principal principal) {
        User user = userService.findByEmail(principal.getName());
        if (user == null || user.getCompany() == null) {
            return "redirect:/managers";
        }

        List<Branch> branches = branchService.getBranchesByCompanyId(user.getCompany().getId());
        model.addAttribute("branches", branches);

        return "company/managers";
    }

    @PostMapping("/update-manager/{branchId}")
    public String updateManager(@PathVariable Integer branchId, @RequestParam(required = false) Integer managerId) {
        if(managerId != null){
            branchService.assignManager(branchId, managerId);
        } else {
            branchService.removeManager(branchId);
        }
        return "redirect:/company/managers";
    }

    @PostMapping("/remove-manager/{id}")
    public String removeManager(@PathVariable("id") Integer branchId) {
        Branch branch = branchService.getBranchById(branchId);
        if (branch != null) {
            branch.setManager(null);
            branchService.updateBranch(branch);
        }
        return "redirect:/company/managers";
    }

}
