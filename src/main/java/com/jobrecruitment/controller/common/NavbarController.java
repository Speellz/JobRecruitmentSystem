package com.jobrecruitment.controller.common;

import com.jobrecruitment.model.User;
import com.jobrecruitment.model.common.UserNotification;
import com.jobrecruitment.service.common.UserNotificationService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class NavbarController {

    private final UserNotificationService notificationService;

    @ModelAttribute
    public void addNotifications(Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser != null) {
            List<UserNotification> notifications = notificationService.getNotificationsByUserId(loggedInUser.getId());
            model.addAttribute("notifications", notifications);
        }
    }
}
