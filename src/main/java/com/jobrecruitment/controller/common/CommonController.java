package com.jobrecruitment.controller.common;

import com.jobrecruitment.model.User;
import com.jobrecruitment.model.applicant.Certification;
import com.jobrecruitment.model.applicant.Education;
import com.jobrecruitment.model.applicant.EmploymentHistory;
import com.jobrecruitment.model.applicant.UserSkill;
import com.jobrecruitment.model.recruiter.Recruiter;
import com.jobrecruitment.model.recruiter.RecruiterAnalytics;
import com.jobrecruitment.repository.UserRepository;
import com.jobrecruitment.repository.applicant.CertificationRepository;
import com.jobrecruitment.repository.applicant.EducationRepository;
import com.jobrecruitment.repository.applicant.EmploymentHistoryRepository;
import com.jobrecruitment.repository.applicant.UserSkillRepository;
import com.jobrecruitment.repository.recruiter.RecruiterAnalyticsRepository;
import com.jobrecruitment.repository.recruiter.RecruiterRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class CommonController {

    private final EducationRepository educationRepository;
    private final EmploymentHistoryRepository employmentHistoryRepository;
    private final CertificationRepository certificationRepository;
    private final UserSkillRepository userSkillRepository;
    private final UserRepository userRepository;
    private final RecruiterRepository recruiterRepository;
    private final RecruiterAnalyticsRepository recruiterAnalyticsRepository;

    @PostMapping("/set-role")
    public String setRoleType(@RequestParam String roleType,
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {
        if ("business".equals(roleType)) {
            session.setAttribute("roleType", "business");
        } else {
            session.removeAttribute("roleType");
        }
        redirectAttributes.addFlashAttribute("success", "Role updated successfully.");
        return "redirect:/";
    }

    @GetMapping("/profile")
    public String userProfile(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) {
            return "redirect:/auth/login";
        }

        model.addAttribute("user", user);

        if (user.getRole().name().equals("APPLICANT")) {
            List<Education> educationList = educationRepository.findByUserId(user.getId());
            List<EmploymentHistory> employmentList = employmentHistoryRepository.findByUserId(user.getId());
            List<Certification> certificationList = certificationRepository.findByUserId(user.getId());
            List<UserSkill> skillList = userSkillRepository.findByUserId(user.getId());

            model.addAttribute("educationList", educationList);
            model.addAttribute("employmentList", employmentList);
            model.addAttribute("certificationList", certificationList);
            model.addAttribute("skillList", skillList);
        }

        if (user.getRole().name().equals("RECRUITER")) {
            Recruiter recruiter = recruiterRepository.findByUserId(user.getId());
            if (recruiter != null) {
                RecruiterAnalytics analytics = recruiterAnalyticsRepository.findByRecruiterId(recruiter.getId().longValue());
                model.addAttribute("recruiter", recruiter);
                model.addAttribute("analytics", analytics);
            }
        }

        return "common/profile";
    }


    @PostMapping("/applicant/upload-cv")
    public String uploadCv(@RequestParam("cvFile") MultipartFile cvFile,
                           HttpSession session,
                           RedirectAttributes redirectAttributes) {

        User user = (User) session.getAttribute("loggedUser");
        if (user == null || user.getRole() != User.Role.APPLICANT) {
            redirectAttributes.addFlashAttribute("error", "Unauthorized access.");
            return "redirect:/auth/login";
        }

        if (cvFile.isEmpty() || !cvFile.getOriginalFilename().endsWith(".pdf")) {
            redirectAttributes.addFlashAttribute("error", "Please upload a valid PDF file.");
            return "redirect:/profile";
        }

        String uploadDir = "C:/cv-uploads/";
        File dir = new File(uploadDir);
        if (!dir.exists()) dir.mkdirs();

        String fileName = UUID.randomUUID() + "_" + cvFile.getOriginalFilename();
        File savedFile = new File(uploadDir + fileName);

        try {
            cvFile.transferTo(savedFile);
            user.setCvUrl("/uploads/cv/" + fileName);
            userRepository.save(user);
            redirectAttributes.addFlashAttribute("success", "CV uploaded successfully.");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Failed to upload CV.");
        }

        return "redirect:/profile";
    }

    @PostMapping("/profile/upload-photo")
    public String uploadProfilePhoto(@RequestParam("photoFile") MultipartFile photoFile,
                                     HttpSession session,
                                     RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) return "redirect:/auth/login";
        if (photoFile.isEmpty() || !(photoFile.getOriginalFilename().endsWith(".png")
                || photoFile.getOriginalFilename().endsWith(".jpg")
                || photoFile.getOriginalFilename().endsWith(".jpeg"))) {
            redirectAttributes.addFlashAttribute("error", "Please upload a valid image file (PNG/JPG/JPEG).");
            return "redirect:/profile";
        }

        String ext = photoFile.getOriginalFilename().substring(photoFile.getOriginalFilename().lastIndexOf('.'));
        String fileName = "profile_" + user.getId() + "_" + System.currentTimeMillis() + ext;
        Path uploadPath = Paths.get("src/main/webapp/uploads/profile");
        try {
            if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(photoFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            user.setProfileImageUrl("/uploads/profile/" + fileName);
            userRepository.save(user);
            redirectAttributes.addFlashAttribute("success", "Profile photo uploaded successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Failed to upload photo.");
        }
        return "redirect:/profile";
    }

    @PostMapping("/profile/remove-photo")
    public String removeProfilePhoto(HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) return "redirect:/auth/login";
        String photoUrl = user.getProfileImageUrl();
        if (photoUrl != null && !photoUrl.isEmpty()) {
            Path filePath = Paths.get("src/main/webapp" + photoUrl);
            try {
                if (Files.exists(filePath)) Files.delete(filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        user.setProfileImageUrl(null);
        userRepository.save(user);
        redirectAttributes.addFlashAttribute("success", "Profile photo removed.");
        return "redirect:/profile";
    }
}
