package com.jobrecruitment.controller.applicant;

import com.jobrecruitment.model.User;
import com.jobrecruitment.model.applicant.Education;
import com.jobrecruitment.repository.applicant.EducationRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/applicant/education")
public class EducationController {

    private final EducationRepository educationRepository;

    @PostMapping("/add")
    public String addEducation(@ModelAttribute Education education, HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) return "redirect:/auth/login";

        try {
            education.setUser(user);
            educationRepository.save(education);
            redirectAttributes.addFlashAttribute("success", "Education added successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to add education.");
        }

        return "redirect:/profile";
    }

    @PostMapping("/update/{id}")
    public String updateEducation(@PathVariable Integer id,
                                  @ModelAttribute Education updatedEdu,
                                  HttpSession session,
                                  RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) return "redirect:/auth/login";

        Education edu = educationRepository.findById(id).orElse(null);
        if (edu != null && edu.getUser().getId().equals(user.getId())) {
            try {
                edu.setInstitutionName(updatedEdu.getInstitutionName());
                edu.setDegree(updatedEdu.getDegree());
                edu.setFieldOfStudy(updatedEdu.getFieldOfStudy());
                edu.setStartDate(updatedEdu.getStartDate());
                edu.setEndDate(updatedEdu.getEndDate());
                educationRepository.save(edu);
                redirectAttributes.addFlashAttribute("success", "Education updated successfully.");
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("error", "Failed to update education.");
            }
        } else {
            redirectAttributes.addFlashAttribute("error", "Unauthorized or invalid education entry.");
        }

        return "redirect:/profile";
    }

    @PostMapping("/delete/{id}")
    public String deleteEducation(@PathVariable Integer id, HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) return "redirect:/auth/login";

        Education edu = educationRepository.findById(id).orElse(null);
        if (edu != null && edu.getUser().getId().equals(user.getId())) {
            try {
                educationRepository.delete(edu);
                redirectAttributes.addFlashAttribute("success", "Education deleted successfully.");
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("error", "Failed to delete education.");
            }
        } else {
            redirectAttributes.addFlashAttribute("error", "Unauthorized or invalid education entry.");
        }

        return "redirect:/profile";
    }
}
