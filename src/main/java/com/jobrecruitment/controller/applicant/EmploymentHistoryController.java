package com.jobrecruitment.controller.applicant;

import com.jobrecruitment.model.User;
import com.jobrecruitment.model.applicant.EmploymentHistory;
import com.jobrecruitment.repository.applicant.EmploymentHistoryRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/applicant/employment")
public class EmploymentHistoryController {

    private final EmploymentHistoryRepository employmentHistoryRepository;

    @PostMapping("/add")
    public String addEmployment(@ModelAttribute EmploymentHistory employment, HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) return "redirect:/auth/login";

        try {
            employment.setUser(user);
            employmentHistoryRepository.save(employment);
            redirectAttributes.addFlashAttribute("success", "Employment added successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to add employment.");
        }

        return "redirect:/profile";
    }

    @PostMapping("/update/{id}")
    public String updateEmployment(@PathVariable Integer id,
                                   @ModelAttribute EmploymentHistory updatedEmp,
                                   HttpSession session,
                                   RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) return "redirect:/auth/login";

        EmploymentHistory emp = employmentHistoryRepository.findById(id).orElse(null);
        if (emp != null && emp.getUser().getId().equals(user.getId())) {
            try {
                emp.setCompanyName(updatedEmp.getCompanyName());
                emp.setCompanyName(updatedEmp.getCompanyName());
                emp.setJobTitle(updatedEmp.getJobTitle());
                emp.setStartDate(updatedEmp.getStartDate());
                emp.setEndDate(updatedEmp.getEndDate());
                emp.setDescription(updatedEmp.getDescription());
                employmentHistoryRepository.save(emp);
                redirectAttributes.addFlashAttribute("success", "Employment history updated successfully.");
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("error", "Failed to update employment.");
            }
        } else {
            redirectAttributes.addFlashAttribute("error", "Unauthorized or invalid entry.");
        }

        return "redirect:/profile";
    }

    @PostMapping("/delete/{id}")
    public String deleteEmployment(@PathVariable Integer id, HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) return "redirect:/auth/login";

        EmploymentHistory emp = employmentHistoryRepository.findById(id).orElse(null);
        if (emp != null && emp.getUser().getId().equals(user.getId())) {
            try {
                employmentHistoryRepository.delete(emp);
                redirectAttributes.addFlashAttribute("success", "Employment deleted successfully.");
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("error", "Failed to delete employment.");
            }
        } else {
            redirectAttributes.addFlashAttribute("error", "Unauthorized or invalid entry.");
        }

        return "redirect:/profile";
    }
}
