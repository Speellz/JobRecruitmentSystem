package com.jobrecruitment.controller.applicant;

import com.jobrecruitment.model.User;
import com.jobrecruitment.model.applicant.Certification;
import com.jobrecruitment.repository.applicant.CertificationRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/applicant/certifications")
public class CertificationController {

    private final CertificationRepository certificationRepository;

    @PostMapping("/add")
    public String addCertification(@ModelAttribute Certification certification,
                                   HttpSession session,
                                   RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) return "redirect:/auth/login";

        try {
            certification.setUser(user);
            certificationRepository.save(certification);
            redirectAttributes.addFlashAttribute("success", "Certification added.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to add certification.");
        }

        return "redirect:/profile";
    }

    @PostMapping("/update/{id}")
    public String updateCertification(@PathVariable Integer id,
                                      @ModelAttribute Certification updatedCert,
                                      HttpSession session,
                                      RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) return "redirect:/auth/login";

        Certification cert = certificationRepository.findById(id).orElse(null);
        if (cert != null && cert.getUser().getId().equals(user.getId())) {
            try {
                cert.setCertificationName(updatedCert.getCertificationName());
                cert.setIssuedBy(updatedCert.getIssuedBy());
                cert.setIssueDate(updatedCert.getIssueDate());
                cert.setExpirationDate(updatedCert.getExpirationDate());
                certificationRepository.save(cert);
                redirectAttributes.addFlashAttribute("success", "Certification updated.");
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("error", "Failed to update certification.");
            }
        } else {
            redirectAttributes.addFlashAttribute("error", "Unauthorized or invalid certification entry.");
        }

        return "redirect:/profile";
    }

    @PostMapping("/delete/{id}")
    public String deleteCertification(@PathVariable Integer id,
                                      HttpSession session,
                                      RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) return "redirect:/auth/login";

        Certification cert = certificationRepository.findById(id).orElse(null);
        if (cert != null && cert.getUser().getId().equals(user.getId())) {
            try {
                certificationRepository.delete(cert);
                redirectAttributes.addFlashAttribute("success", "Certification deleted.");
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("error", "Failed to delete certification.");
            }
        } else {
            redirectAttributes.addFlashAttribute("error", "Unauthorized or invalid certification entry.");
        }

        return "redirect:/profile";
    }
}
