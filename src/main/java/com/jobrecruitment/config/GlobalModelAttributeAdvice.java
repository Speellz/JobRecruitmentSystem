package com.jobrecruitment.config;

import com.jobrecruitment.model.User;
import com.jobrecruitment.model.common.Message;
import com.jobrecruitment.model.common.UserNotification;
import com.jobrecruitment.repository.common.MessageRepository;
import com.jobrecruitment.service.common.UserNotificationService;
import com.jobrecruitment.service.common.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;
import java.util.Collections;
import java.util.List;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalModelAttributeAdvice {

    private final UserNotificationService notificationService;
    private final UserService userService;
    private final MessageRepository messageRepository;

    @ModelAttribute("notifications")
    public List<UserNotification> globalNotifications(Principal principal) {
        if (principal == null) return Collections.emptyList();
        User user = userService.findByEmail(principal.getName());
        if (user == null) return Collections.emptyList();
        return notificationService.getNotificationsByUserId(user.getId());
    }

    @ModelAttribute("latestMessages")
    public List<Message> globalMessages(HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) return Collections.emptyList();
        return messageRepository.findTop5ByReceiverIdOrderBySentAtDesc(user.getId());
    }

}
