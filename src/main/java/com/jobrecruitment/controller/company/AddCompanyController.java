package com.jobrecruitment.controller.company;

import com.jobrecruitment.model.company.Company;
import com.jobrecruitment.model.User;
import com.jobrecruitment.service.company.CompanyService;
import com.jobrecruitment.service.common.UserService;
import com.jobrecruitment.service.common.UserNotificationService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/company")
@RequiredArgsConstructor
public class AddCompanyController {

    private final CompanyService companyService;
    private final UserService userService;
    private final UserNotificationService userNotificationService;

    @PostMapping("/add-company")
    public String registerCompany(@ModelAttribute("company") @Valid Company company,
                                  BindingResult bindingResult,
                                  Principal principal,
                                  HttpSession session,
                                  RedirectAttributes redirectAttributes) {

        if (principal == null) {
            redirectAttributes.addFlashAttribute("error", "You must be logged in to register a company.");
            return "redirect:/auth/login";
        }

        User user = userService.findByEmail(principal.getName());
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "User not found.");
            return "redirect:/auth/login";
        }

        if (user.getCompany() != null) {
            session.setAttribute("userCompany", user.getCompany());
            redirectAttributes.addFlashAttribute("error", "You already have a company.");
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

            userNotificationService.sendNotification(
                    user,
                    "Your company registration is submitted and pending admin approval.",
                    "/company/dashboard"
            );

            redirectAttributes.addFlashAttribute("success", "Company registered successfully and pending admin approval.");
            return "redirect:/";
        } else {
            redirectAttributes.addFlashAttribute("error", "A company with this email already exists.");
            return "redirect:/";
        }
    }
}
