package com.jobrecruitment.controller.auth;

import com.jobrecruitment.model.User;
import com.jobrecruitment.model.company.Company;
import com.jobrecruitment.model.company.CompanyStatus;
import com.jobrecruitment.repository.company.CompanyRepository;
import com.jobrecruitment.service.common.UserService;
import com.jobrecruitment.service.company.CompanyService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final CompanyService companyService;
    private final CompanyRepository companyRepository;

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
    public String registerApplicant(@ModelAttribute("user") User user,
                                    BindingResult result,
                                    RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) return "common/signup";

        if (userService.findByEmail(user.getEmail()) != null) {
            redirectAttributes.addFlashAttribute("error", "Email is already in use.");
            return "redirect:/auth/signup";
        }

        user.setPassword(userService.encodePassword(user.getPassword()));
        user.setRole(User.Role.APPLICANT);
        userService.saveUser(user);

        redirectAttributes.addFlashAttribute("success", "Signup successful. Please log in.");
        return "redirect:/auth/login";
    }

    @PostMapping("/company-signup")
    public String registerCompany(@ModelAttribute("user") User user,
                                  BindingResult result,
                                  RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) return "common/company-signup";

        if (userService.findByEmail(user.getEmail()) != null) {
            redirectAttributes.addFlashAttribute("error", "Email is already in use.");
            return "redirect:/auth/company-signup";
        }

        user.setPassword(userService.encodePassword(user.getPassword()));
        user.setRole(User.Role.COMPANY);

        User savedUser = userService.saveUser(user);

        Company company = new Company();
        company.setName("Unnamed Company");
        company.setEmail(savedUser.getEmail());
        company.setStatus(CompanyStatus.NEW);

        company.setOwner(savedUser);

        companyRepository.save(company);

        savedUser.setCompany(company);
        userService.saveUser(savedUser);

        redirectAttributes.addFlashAttribute("success", "Company account created. Please log in.");
        return "redirect:/auth/login";
    }

    @PostMapping("/set-role")
    @ResponseBody
    public String setRole(@RequestParam("roleType") String roleType, HttpSession session) {
        session.setAttribute("roleType", roleType);
        return "role set to " + roleType;
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
