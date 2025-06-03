package com.jobrecruitment.controller.company;

import com.jobrecruitment.model.company.Company;
import com.jobrecruitment.model.User;
import com.jobrecruitment.service.company.CompanyService;
import com.jobrecruitment.service.common.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import lombok.RequiredArgsConstructor;

import java.security.Principal;

@Controller
@RequestMapping("/company")
@RequiredArgsConstructor
public class AddCompanyController {

    private final CompanyService companyService;

    private final UserService userService;

    @PostMapping("/add-company")
    public String registerCompany(@ModelAttribute("company") @Valid Company company,
                                  BindingResult bindingResult,
                                  Principal principal,
                                  HttpSession session,
                                  RedirectAttributes redirectAttributes) {

        if (principal == null) {
            return "redirect:/auth/login";
        }

        User user = userService.findByEmail(principal.getName());
        if (user == null) {
            return "redirect:/auth/login";
        }

        if (user.getCompany() != null) {
            session.setAttribute("userCompany", user.getCompany());
            return "redirect:/";
        }

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Please fill all required fields.");
            return "redirect:/";
        }

        boolean isRegistered = companyService.registerCompany(company, user);
        if (isRegistered) {
            user.setCompany(company);
            userService.saveUser(user);
            session.setAttribute("userCompany", company);
            redirectAttributes.addFlashAttribute("success", "Company registered successfully!");
            return "redirect:/";
        } else {
            redirectAttributes.addFlashAttribute("error", "A company with this email already exists.");
            return "redirect:/";
        }
    }
}
