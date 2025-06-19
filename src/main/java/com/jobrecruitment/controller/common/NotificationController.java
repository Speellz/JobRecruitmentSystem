package com.jobrecruitment.controller.common;

import com.jobrecruitment.model.User;
import com.jobrecruitment.model.common.UserNotification;
import com.jobrecruitment.service.common.UserNotificationService;
import com.jobrecruitment.service.common.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/notifications")
public class NotificationController {

    private final UserNotificationService notificationService;
    private final UserService userService;


    @GetMapping
    public String viewNotifications(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) return "redirect:/auth/login";

        model.addAttribute("notifications", notificationService.getNotificationsByUserId(user.getId()));
        return "common/notifications";
    }

    @PostMapping("/delete/{id}")
    public String deleteNotification(@PathVariable Long id, HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) return "redirect:/auth/login";

        notificationService.deleteNotification(id);
        return "redirect:/notifications";
    }

    @PostMapping("/clear")
    public String clearAllNotifications(HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) return "redirect:/auth/login";

        notificationService.deleteAllByUserId(user.getId());
        return "redirect:/notifications";
    }

    @GetMapping("/json")
    @ResponseBody
    public List<UserNotification> getLatestNotifications(Principal principal) {
        if (principal == null) {
            return List.of();
        }

        try {
            String email = principal.getName();
            User user = userService.findByEmail(email);

            if (user == null) {
                return List.of();
            }

            return notificationService.getNotificationsByUserId(user.getId())
                    .stream()
                    .limit(5)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            // Log the error
            e.printStackTrace();
            return List.of();
        }
    }

}
