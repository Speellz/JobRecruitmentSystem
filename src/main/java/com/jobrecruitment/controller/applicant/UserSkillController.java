package com.jobrecruitment.controller.applicant;

import com.jobrecruitment.model.User;
import com.jobrecruitment.model.applicant.UserSkill;
import com.jobrecruitment.repository.applicant.UserSkillRepository;
import com.jobrecruitment.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/skills")
public class UserSkillController {

    private final UserSkillRepository skillRepository;
    private final UserRepository userRepository;

    @PostMapping("/add")
    public String addSkill(@RequestParam String skillName, HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) return "redirect:/auth/login";

        UserSkill skill = new UserSkill();
        skill.setSkillName(skillName);
        skill.setUser(user);
        skillRepository.save(skill);

        return "redirect:/profile";
    }

    @PostMapping("/update/{id}")
    public String updateSkill(@PathVariable Integer id,
                              @RequestParam String skillName,
                              HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) return "redirect:/auth/login";

        UserSkill skill = skillRepository.findById(id).orElse(null);
        if (skill != null && skill.getUser().getId().equals(user.getId())) {
            skill.setSkillName(skillName);
            skillRepository.save(skill);
        }

        return "redirect:/profile";
    }

    @PostMapping("/delete/{id}")
    public String deleteSkill(@PathVariable Integer id, HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) return "redirect:/auth/login";

        UserSkill skill = skillRepository.findById(id).orElse(null);
        if (skill != null && skill.getUser().getId().equals(user.getId())) {
            skillRepository.delete(skill);
        }

        return "redirect:/profile";
    }
}
