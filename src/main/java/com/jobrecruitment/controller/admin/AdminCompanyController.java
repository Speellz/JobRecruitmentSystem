package com.jobrecruitment.controller.admin;

import com.jobrecruitment.model.company.Branch;
import com.jobrecruitment.model.company.Company;
import com.jobrecruitment.model.User;
import com.jobrecruitment.service.company.BranchService;
import com.jobrecruitment.service.company.CompanyService;
import com.jobrecruitment.service.common.UserNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/companies")
@RequiredArgsConstructor
public class AdminCompanyController {

    private final CompanyService companyService;
    private final BranchService branchService;
    private final UserNotificationService userNotificationService;

    @GetMapping
    public String listApprovedCompanies(Model model,
                                        @ModelAttribute("success") String success,
                                        @ModelAttribute("error") String error) {
        List<Company> approvedCompanies = companyService.getApprovedCompanies();
        model.addAttribute("companies", approvedCompanies);
        return "admin/companies";
    }

    @GetMapping("/{id}")
    public String viewCompanyDetail(@PathVariable("id") Integer id, Model model,
                                    @ModelAttribute("success") String success,
                                    @ModelAttribute("error") String error) {
        Company company = companyService.findById(id);
        if (company == null) {
            return "redirect:/admin/companies";
        }
        List<Branch> branches = branchService.getBranchesByCompanyId(id);
        model.addAttribute("company", company);
        model.addAttribute("branches", branches);
        return "admin/company-detail";
    }

    @PostMapping("/{id}/delete")
    public String deleteCompany(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        Company company = companyService.findById(id);

        boolean result = companyService.deleteCompanyById(id);
        if (result) {
            redirectAttributes.addFlashAttribute("success", "Company deleted successfully.");

            if (company != null && company.getOwner() != null) {
                User owner = company.getOwner();
                userNotificationService.sendNotification(
                        owner,
                        "Your company \"" + company.getName() + "\" has been deleted by the admin.",
                        "/"
                );
            }
        } else {
            redirectAttributes.addFlashAttribute("error", "Failed to delete company.");
        }

        return "redirect:/admin/companies";
    }
}
