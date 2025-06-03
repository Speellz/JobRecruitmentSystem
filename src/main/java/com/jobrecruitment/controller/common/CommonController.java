package com.jobrecruitment.controller.common;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CommonController {

    @PostMapping("/set-role")
    public String setRoleType(@RequestParam String roleType, HttpSession session) {
        if ("business".equals(roleType)) {
            session.setAttribute("roleType", "business");
        } else {
            session.removeAttribute("roleType");
        }
        return "redirect:/";
    }
}
