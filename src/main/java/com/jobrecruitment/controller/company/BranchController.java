package com.jobrecruitment.controller.company;

import com.jobrecruitment.model.User;
import com.jobrecruitment.model.company.Branch;
import com.jobrecruitment.service.company.BranchService;
import com.jobrecruitment.service.common.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/company/branches")
public class BranchController {

    @Autowired
    private BranchService branchService;

    @Autowired
    private UserService userService;

    @GetMapping
    public String listBranches(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/auth/login";
        }

        User user = userService.findByEmail(principal.getName());
        if (user == null || user.getCompany() == null) {
            return "redirect:/";
        }

        List<Branch> branches = branchService.getBranchesByCompanyId(user.getCompany().getId());
        model.addAttribute("branches", branches);

        return "company/branches";
    }

    @PostMapping("/add")
    public String addBranch(@RequestParam String name,
                            @RequestParam String location,
                            Principal principal) {
        if (principal == null) {
            return "redirect:/auth/login";
        }

        User user = userService.findByEmail(principal.getName());
        if (user == null || user.getCompany() == null) {
            return "redirect:/";
        }

        Branch branch = new Branch();
        branch.setName(name);
        branch.setLocation(location);
        branch.setCompany(user.getCompany());
        branch.setCreatedAt(LocalDateTime.now());

        branchService.addBranch(branch);

        return "redirect:/company/branches";
    }

    @PostMapping("/delete/{id}")
    public String deleteBranch(@PathVariable Integer id) {
        branchService.deleteBranch(id);
        return "redirect:/company/branches";
    }
}
