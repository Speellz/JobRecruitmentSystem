package com.jobrecruitment.controller.admin;

import com.jobrecruitment.model.recruiter.Recruiter;
import com.jobrecruitment.model.company.Branch;
import com.jobrecruitment.model.User;
import com.jobrecruitment.service.company.BranchService;
import com.jobrecruitment.service.company.CompanyService;
import com.jobrecruitment.service.recruiter.RecruiterService;
import com.jobrecruitment.service.common.UserNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/branches")
@RequiredArgsConstructor
public class AdminBranchController {

    private final BranchService branchService;
    private final CompanyService companyService;
    private final RecruiterService recruiterService;
    private final UserNotificationService userNotificationService;

    @GetMapping("/{branchId}")
    public String viewBranchDetail(@PathVariable("branchId") Integer branchId, Model model,
                                   @ModelAttribute("success") String success,
                                   @ModelAttribute("error") String error) {
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
    public String assignManager(@PathVariable Integer branchId,
                                @RequestParam Integer managerRecruiterId,
                                RedirectAttributes redirectAttributes) {
        branchService.assignManager(branchId, managerRecruiterId);
        redirectAttributes.addFlashAttribute("success", "Manager successfully assigned to branch.");

        Recruiter assignedManager = recruiterService.getById(managerRecruiterId);
        if (assignedManager != null && assignedManager.getUser() != null) {
            User user = assignedManager.getUser();
            userNotificationService.sendNotification(
                    user,
                    "You have been assigned as the Branch Manager.",
                    "/recruiter/dashboard"
            );
        }

        return "redirect:/admin/branches/" + branchId;
    }
}
