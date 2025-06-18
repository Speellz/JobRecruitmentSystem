package com.jobrecruitment.controller.recruiter;

import com.jobrecruitment.model.User;
import com.jobrecruitment.model.company.Branch;
import com.jobrecruitment.model.company.Company;
import com.jobrecruitment.model.recruiter.Recruiter;
import com.jobrecruitment.model.recruiter.RecruiterRole;
import com.jobrecruitment.repository.company.BranchRepository;
import com.jobrecruitment.repository.UserRepository;
import com.jobrecruitment.repository.recruiter.RecruiterRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class MyCompanyController {

    private final RecruiterRepository recruiterRepository;
    private final BranchRepository branchRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/recruiter/my-company")
    public String viewMyCompany(Model model, HttpSession session) {
        User loggedUser = (User) session.getAttribute("loggedUser");

        if (loggedUser == null) {
            return "redirect:/login";
        }

        Optional<Recruiter> recruiterOpt = recruiterRepository.findByUserEmail(loggedUser.getEmail());
        if (recruiterOpt.isEmpty()) {
            return "redirect:/";
        }

        Recruiter recruiter = recruiterOpt.get();
        Branch branch = recruiter.getBranch();
        Company company = recruiter.getCompany();

        boolean isManager = branch.getManager() != null && branch.getManager().getId().equals(loggedUser.getId());
        List<Recruiter> branchRecruiters = recruiterRepository.findByBranchId(branch.getId());

        model.addAttribute("company", company);
        model.addAttribute("branch", branch);
        model.addAttribute("manager", branch.getManager());
        model.addAttribute("recruiters", branchRecruiters);
        model.addAttribute("isManager", isManager);

        return "recruiter/my-company";
    }

    @PostMapping("/recruiter/add-by-manager")
    public String addRecruiter(@RequestParam String name,
                               @RequestParam String email,
                               @RequestParam String phone,
                               @RequestParam String password,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {

        User loggedUser = (User) session.getAttribute("loggedUser");
        if (loggedUser == null) return "redirect:/login";

        Optional<Recruiter> recruiterOpt = recruiterRepository.findByUserEmail(loggedUser.getEmail());
        if (recruiterOpt.isEmpty()) return "redirect:/";

        Recruiter manager = recruiterOpt.get();
        Branch branch = manager.getBranch();

        if (branch.getManager() == null || !branch.getManager().getId().equals(loggedUser.getId())) {
            redirectAttributes.addFlashAttribute("error", "You are not authorized to add a recruiter to this branch.");
            return "redirect:/recruiter/my-company";
        }

        if (userRepository.findByEmail(email).isPresent()) {
            redirectAttributes.addFlashAttribute("error", "This email is already in use.");
            return "redirect:/recruiter/my-company";
        }

        User newUser = new User();
        newUser.setName(name);
        newUser.setEmail(email);
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setRole(User.Role.RECRUITER);
        newUser.setCompany(manager.getCompany());

        userRepository.save(newUser);

        Recruiter newRecruiter = new Recruiter();
        newRecruiter.setUser(newUser);
        newRecruiter.setCompany(manager.getCompany());
        newRecruiter.setBranch(branch);
        newRecruiter.setRole(RecruiterRole.RECRUITER);
        newRecruiter.setPhone(phone);

        recruiterRepository.save(newRecruiter);

        redirectAttributes.addFlashAttribute("success", "Recruiter successfully added.");
        return "redirect:/recruiter/my-company";
    }

    @PostMapping("/recruiter/delete/{id}")
    public String deleteRecruiter(@PathVariable Integer id, HttpSession session, RedirectAttributes redirectAttributes) {
        User loggedUser = (User) session.getAttribute("loggedUser");
        if (loggedUser == null) return "redirect:/login";

        Optional<Recruiter> recruiterOpt = recruiterRepository.findByUserEmail(loggedUser.getEmail());
        if (recruiterOpt.isEmpty()) return "redirect:/";

        Recruiter manager = recruiterOpt.get();
        Branch branch = manager.getBranch();

        if (branch.getManager() == null || !branch.getManager().getId().equals(loggedUser.getId())) {
            redirectAttributes.addFlashAttribute("error", "You are not authorized to delete recruiters from this branch.");
            return "redirect:/recruiter/my-company";
        }

        Optional<Recruiter> targetOpt = recruiterRepository.findById(id);
        if (targetOpt.isPresent() && targetOpt.get().getBranch().getId().equals(branch.getId())) {
            recruiterRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Recruiter successfully deleted.");
        } else {
            redirectAttributes.addFlashAttribute("error", "Recruiter not found or does not belong to your branch.");
        }

        return "redirect:/recruiter/my-company";
    }
}
