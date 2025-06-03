package com.jobrecruitment.controller.company;

import com.jobrecruitment.model.recruiter.Recruiter;
import com.jobrecruitment.model.User;
import com.jobrecruitment.model.company.Branch;
import com.jobrecruitment.model.company.Company;
import com.jobrecruitment.model.recruiter.RecruiterRole;
import com.jobrecruitment.repository.UserRepository;
import com.jobrecruitment.repository.recruiter.RecruiterRepository;
import com.jobrecruitment.service.company.BranchService;
import com.jobrecruitment.service.common.UserService;
import com.jobrecruitment.service.recruiter.RecruiterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/company/recruiters")
public class CompanyRecruiterController {

    @Autowired
    private RecruiterService recruiterService;

    @Autowired
    private BranchService branchService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RecruiterRepository recruiterRepository;

    @GetMapping
    public String listRecruiters(Model model, Principal principal) {
        User user = userService.findByEmail(principal.getName());
        Integer companyId = user.getCompany().getId();

        List<Branch> branches = branchService.getBranchesByCompanyId(companyId);
        List<Recruiter> recruiters = recruiterService.findByCompanyId(companyId);

        model.addAttribute("branches", branches);
        model.addAttribute("recruiters", recruiters);
        return "company/recruiters";
    }

    @GetMapping("/add")
    public String showAddRecruiterForm(Model model, Principal principal) {
        User user = userService.findByEmail(principal.getName());
        List<Branch> branches = branchService.getBranchesByCompanyId(user.getCompany().getId());
        model.addAttribute("branches", branches);
        model.addAttribute("recruiter", new Recruiter());
        return "company/add-recruiter";
    }

    @PostMapping("/add")
    public String addRecruiter(@ModelAttribute Recruiter recruiter,
                               @RequestParam("branchId") Integer branchId,
                               Principal principal,
                               Model model,
                               @RequestParam("phone") String phone) {
        User currentUser = userService.findByEmail(principal.getName());

        if (userRepository.findByEmail(recruiter.getUser().getEmail()).isPresent()) {
            model.addAttribute("error", "This email is already used.");
            model.addAttribute("branches", branchService.getBranchesByCompanyId(currentUser.getCompany().getId()));
            return "company/add-recruiter";
        }

        if (branchId == null || branchId == 0) {
            model.addAttribute("error", "Branch selection is required.");
            model.addAttribute("branches", branchService.getBranchesByCompanyId(currentUser.getCompany().getId()));
            return "company/add-recruiter";
        }

        User newUser = recruiter.getUser();
        newUser.setRole(User.Role.RECRUITER);
        newUser.setCompany(currentUser.getCompany());
        userRepository.save(newUser);

        recruiter.setUser(newUser);
        recruiter.setCompany(currentUser.getCompany());
        recruiter.setBranch(branchService.getBranchById(branchId));
        recruiter.setRole(RecruiterRole.RECRUITER);
        recruiter.setCreatedAt(LocalDateTime.now());

        recruiterRepository.save(recruiter);

        return "redirect:/company/recruiters?success";
    }


    @PostMapping("/delete/{id}")
    public String deleteRecruiter(@PathVariable("id") Integer id) {
        recruiterService.deleteRecruiter(id);
        return "redirect:/company/recruiters?deleted";
    }
}
