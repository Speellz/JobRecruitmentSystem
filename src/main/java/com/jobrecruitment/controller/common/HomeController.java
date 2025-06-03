package com.jobrecruitment.controller.common;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

    @GetMapping("/")
    public ModelAndView home() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return new ModelAndView("redirect:/admin/dashboard");
        }
        return new ModelAndView("common/index");
    }

    @GetMapping("/login")
    public String loginPage() {
        return "common/login";
    }

    @GetMapping("/signup")
    public String signupPage() {
        return "common/signup";
    }
}

