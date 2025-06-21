package com.jobrecruitment.controller.company;

import com.jobrecruitment.model.User;
import com.jobrecruitment.model.company.Company;
import com.jobrecruitment.model.company.CompanyStatus;
import com.jobrecruitment.repository.company.CompanyRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/company")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyRepository companyRepository;

    @GetMapping("/create")
    public String showCompanyForm(HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null || user.getCompany() == null) {
            redirectAttributes.addFlashAttribute("error", "You must be logged in to view this page.");
            return "redirect:/auth/login";
        }

        Company company = user.getCompany();
        if (company.getStatus() == CompanyStatus.APPROVED) {
            redirectAttributes.addFlashAttribute("info", "Company information already submitted and approved.");
            return "redirect:/company/dashboard";
        } else if (company.getStatus() == CompanyStatus.REJECTED) {
            redirectAttributes.addFlashAttribute("warning", "Your company registration was rejected. Please update your information.");
            return "redirect:/company/rejected";
        }

        return "company/company-form";
    }


    @PostMapping("/create")
    public String submitCompanyInfo(@RequestParam("name") String name,
                                    @RequestParam("email") String email,
                                    @RequestParam("phone") String phone,
                                    @RequestParam("website") String website,
                                    HttpSession session,
                                    RedirectAttributes redirectAttributes) {

        User user = (User) session.getAttribute("loggedUser");
        if (user == null || user.getCompany() == null) {
            redirectAttributes.addFlashAttribute("error", "User or company not found.");
            return "redirect:/";
        }

        Company company = user.getCompany();
        company.setName(name);
        company.setEmail(email);
        company.setPhone(phone);
        company.setWebsite(website);
        company.setStatus(CompanyStatus.PENDING);

        companyRepository.save(company);
        session.setAttribute("userCompany", company);

        redirectAttributes.addFlashAttribute("success", "Company details submitted. Waiting for admin approval.");
        return "redirect:/";
    }

    @GetMapping("/rejected")
    public String rejectedPage() {
        return "company/rejected";
    }

}
