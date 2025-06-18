package com.jobrecruitment.controller.company;

import com.jobrecruitment.model.User;
import com.jobrecruitment.model.company.Branch;
import com.jobrecruitment.model.company.Company;
import com.jobrecruitment.model.recruiter.Recruiter;
import com.jobrecruitment.repository.company.BranchRepository;
import com.jobrecruitment.repository.company.CompanyRepository;
import com.jobrecruitment.repository.recruiter.RecruiterRepository;
import com.jobrecruitment.repository.recruiter.JobPostingRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class CompanyDashboardController {

    private final CompanyRepository companyRepository;
    private final BranchRepository branchRepository;
    private final RecruiterRepository recruiterRepository;
    private final JobPostingRepository jobPostingRepository;

    @GetMapping("/company/dashboard")
    public String dashboard(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) {
            return "redirect:/auth/login";
        }

        Company company = companyRepository.findByOwnerId(user.getId()).orElse(null);
        if (company == null) {
            return "redirect:/";
        }

        List<Branch> branches = branchRepository.findByCompanyId(company.getId());
        List<Recruiter> recruiters = recruiterRepository.findByCompanyId(company.getId());

        int totalJobPostings = jobPostingRepository.countByBranchCompanyId(company.getId());

        model.addAttribute("company", company);
        model.addAttribute("branches", branches);
        model.addAttribute("recruiters", recruiters);
        model.addAttribute("totalJobPostings", totalJobPostings);

        return "company/dashboard";
    }


    @PostMapping("/company/upload-logo")
    public String uploadLogo(@RequestParam("logo") MultipartFile file,
                             HttpSession session) throws IOException {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) return "redirect:/auth/login";

        Company company = companyRepository.findByOwnerId(user.getId()).orElse(null);
        if (company == null) return "redirect:/";

        if (!file.isEmpty()) {
            String fileName = "logo_" + company.getId() + "_" + System.currentTimeMillis() + ".png";
            Path uploadPath = Paths.get("src/main/webapp/uploads");

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            company.setLogoPath(fileName);
            companyRepository.save(company);
        }

        return "redirect:/company/dashboard";
    }

    @PostMapping("/company/remove-logo")
    public String removeLogo(HttpSession session) throws IOException {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) return "redirect:/auth/login";

        Company company = companyRepository.findByOwnerId(user.getId()).orElse(null);
        if (company == null || company.getLogoPath() == null) return "redirect:/company/dashboard";

        Path logoPath = Paths.get("src/main/webapp/uploads", company.getLogoPath());
        if (Files.exists(logoPath)) {
            Files.delete(logoPath);
        }

        company.setLogoPath(null);
        companyRepository.save(company);

        return "redirect:/company/dashboard";
    }

    @PostMapping("/company/update-info")
    public String updateCompanyInfo(@RequestParam String name,
                                    @RequestParam String email,
                                    @RequestParam String phone,
                                    @RequestParam String website,
                                    HttpSession session,
                                    RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) return "redirect:/auth/login";

        Company company = companyRepository.findByOwnerId(user.getId()).orElse(null);
        if (company == null) return "redirect:/";

        company.setName(name);
        company.setEmail(email);
        company.setPhone(phone);
        company.setWebsite(website);
        companyRepository.save(company);

        redirectAttributes.addFlashAttribute("success", "Company info updated successfully!");
        return "redirect:/company/dashboard";
    }

}