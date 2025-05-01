package com.jobrecruitment.controller.common;

import com.jobrecruitment.model.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class NavbarController {

    @ModelAttribute
    public void addAttributes(Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("user");
        model.addAttribute("loggedInUser", loggedInUser);
    }
}
