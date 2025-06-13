package com.jobrecruitment.controller.admin;

import com.jobrecruitment.model.company.Branch;
import com.jobrecruitment.model.company.Company;
import com.jobrecruitment.service.company.BranchService;
import com.jobrecruitment.service.company.CompanyService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Controller
@RequestMapping("/admin/companies")
@RequiredArgsConstructor
public class AdminCompanyController {

    private final CompanyService companyService;

    private final BranchService branchService;

    @GetMapping
    public String listApprovedCompanies(Model model) {
        List<Company> approvedCompanies = companyService.getApprovedCompanies();
        model.addAttribute("companies", approvedCompanies);
        return "admin/companies";
    }

    @GetMapping("/{id}")
    public String viewCompanyDetail(@PathVariable("id") Integer id, Model model) {
        Company company = companyService.findById(id);
        if (company == null) {
            return "redirect:/admin/companies";
        }
        List<Branch> branches = branchService.getBranchesByCompanyId(id);
        model.addAttribute("company", company);
        model.addAttribute("branches", branches);
        return "admin/company-detail";
    }
}
