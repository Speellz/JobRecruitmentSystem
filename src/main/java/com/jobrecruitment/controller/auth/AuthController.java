package com.jobrecruitment.controller.auth;

import com.jobrecruitment.model.User;
import com.jobrecruitment.service.common.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @GetMapping("/login")
    public String loginPage() {
        return "common/login";
    }

    @GetMapping("/signup")
    public String signupPage() {
        return "common/signup";
    }

    @GetMapping("/company-signup")
    public String companySignupPage() {
        return "common/company-signup";
    }

    @PostMapping("/signup")
    public String registerApplicant(@ModelAttribute("user") User user, BindingResult result) {
        if (result.hasErrors()) {
            return "common/signup";
        }

        if (userService.findByEmail(user.getEmail()) != null) {
            return "redirect:/auth/signup?error=email-exists";
        }

        user.setPassword(userService.encodePassword(user.getPassword()));
        user.setRole(User.Role.APPLICANT);
        userService.saveUser(user);

        return "redirect:/auth/login?success";
    }

    @PostMapping("/company-signup")
    public String registerCompany(@ModelAttribute("user") User user, BindingResult result) {
        if (result.hasErrors()) {
            return "common/company-signup";
        }

        if (userService.findByEmail(user.getEmail()) != null) {
            return "redirect:/auth/company-signup?error=email-exists";
        }

        user.setPassword(userService.encodePassword(user.getPassword()));
        user.setRole(User.Role.COMPANY);
        userService.saveUser(user);

        return "redirect:/auth/login?success";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
