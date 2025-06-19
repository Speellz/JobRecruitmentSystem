package com.jobrecruitment.controller.company;

import com.jobrecruitment.model.User;
import com.jobrecruitment.model.company.Branch;
import com.jobrecruitment.model.recruiter.Recruiter;
import com.jobrecruitment.service.company.BranchService;
import com.jobrecruitment.service.common.UserNotificationService;
import com.jobrecruitment.service.common.UserService;
import com.jobrecruitment.service.recruiter.RecruiterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    private final UserNotificationService userNotificationService;

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
                            Principal principal,
                            RedirectAttributes redirectAttributes) {
        if (principal == null) return "redirect:/auth/login";

        User user = userService.findByEmail(principal.getName());
        if (user == null || user.getCompany() == null) return "redirect:/";

        Branch branch = new Branch();
        branch.setName(name);
        branch.setLocation(location);
        branch.setCompany(user.getCompany());
        branch.setCreatedAt(LocalDateTime.now());

        branchService.addBranch(branch);

        userNotificationService.sendNotification(
                user,
                "New branch \"" + name + "\" added to your company.",
                "/company/branches"
        );

        redirectAttributes.addFlashAttribute("success", "Branch successfully added.");
        return "redirect:/company/branches";
    }

    @PostMapping("/delete/{id}")
    public String deleteBranch(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        Branch branch = branchService.getBranchById(id);
        branchService.deleteBranch(id);

        if (branch != null && branch.getCompany() != null) {
            List<User> users = userService.findByCompanyId(branch.getCompany().getId());
            for (User user : users) {
                userNotificationService.sendNotification(
                        user,
                        "Branch \"" + branch.getName() + "\" has been deleted.",
                        "/company/branches"
                );
            }
        }

        redirectAttributes.addFlashAttribute("success", "Branch successfully deleted.");
        return "redirect:/company/branches";
    }

    @GetMapping("/edit/{id}")
    public String editBranch(@PathVariable Integer id, Model model) {
        Branch branch = branchService.getBranchById(id);
        if (branch == null) return "redirect:/company/branches";
        model.addAttribute("branch", branch);
        return "company/edit-branch";
    }

    @PostMapping("/update")
    public String updateBranch(@ModelAttribute Branch branch, RedirectAttributes redirectAttributes) {
        branchService.updateBranch(branch);

        if (branch.getCompany() != null) {
            List<User> users = userService.findByCompanyId(branch.getCompany().getId());
            for (User user : users) {
                userNotificationService.sendNotification(
                        user,
                        "Branch \"" + branch.getName() + "\" has been updated.",
                        "/company/branches"
                );
            }
        }

        redirectAttributes.addFlashAttribute("success", "Branch updated successfully.");
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
                                @RequestParam("managerId") Integer managerId,
                                RedirectAttributes redirectAttributes) {

        branchService.assignManager(branchId, managerId);
        Recruiter manager = recruiterService.getById(managerId);
        if (manager != null && manager.getUser() != null) {
            userNotificationService.sendNotification(
                    manager.getUser(),
                    "You have been assigned as manager of branch ID " + branchId + ".",
                    "/recruiter/dashboard"
            );
        }

        redirectAttributes.addFlashAttribute("success", "Branch manager updated successfully.");
        return "redirect:/company/managers";
    }
}
