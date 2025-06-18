package com.jobrecruitment.controller.admin;

import com.jobrecruitment.model.company.Company;
import com.jobrecruitment.model.User;
import com.jobrecruitment.service.company.CompanyService;
import com.jobrecruitment.service.common.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final CompanyService companyService;
    private final UserService userService;

    public AdminController(CompanyService companyService, UserService userService) {
        this.companyService = companyService;
        this.userService = userService;
    }

    @GetMapping("/admin-dashboard")
    public String adminDashboard(Model model, Authentication authentication) {
        if (authentication == null || !authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ADMIN"))) {
            return "redirect:/login";
        }

        List<Company> pendingCompanies = companyService.getPendingCompanies();
        model.addAttribute("pendingCompanies", pendingCompanies);

        return "admin/dashboard";
    }

    @PostMapping("/approve-company/{id}")
    public String approveCompany(@PathVariable int id, RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ADMIN"))) {
            return "redirect:/login";
        }

        String email = authentication.getName();
        User adminUser = userService.findByEmail(email);
        int adminId = adminUser.getId().intValue();

        boolean success = companyService.approveCompany(id, adminId);
        if (success) {
            redirectAttributes.addFlashAttribute("success", "Company approved successfully.");
        } else {
            redirectAttributes.addFlashAttribute("error", "Failed to approve company.");
        }

        return "redirect:/admin/admin-dashboard";
    }

    @PostMapping("/reject-company/{id}")
    public String rejectCompany(@PathVariable int id, RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ADMIN"))) {
            return "redirect:/login";
        }

        boolean success = companyService.rejectCompany(id);
        if (success) {
            redirectAttributes.addFlashAttribute("success", "Company rejected successfully.");
        } else {
            redirectAttributes.addFlashAttribute("error", "Failed to reject company.");
        }

        return "redirect:/admin/admin-dashboard";
    }
}
