package com.jobrecruitment.controller.common;

import com.jobrecruitment.model.User;
import com.jobrecruitment.model.applicant.Application;
import com.jobrecruitment.model.common.Message;
import com.jobrecruitment.repository.UserRepository;
import com.jobrecruitment.repository.applicant.ApplicationRepository;
import com.jobrecruitment.repository.common.MessageRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/messages")
@RequiredArgsConstructor
public class MessagingController {
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ApplicationRepository applicationRepository;

    @GetMapping
    public String messagePage(@RequestParam("userId") Long receiverId, HttpSession session, Model model) {
        User sender = (User) session.getAttribute("loggedUser");
        if (sender == null) return "redirect:/auth/login";
        User receiver = userRepository.findById(receiverId).orElse(null);
        if (receiver == null) return "redirect:/";
        List<Message> messages = messageRepository
                .findBySenderIdAndReceiverIdOrderBySentAt(sender.getId(), receiverId);
        model.addAttribute("receiver", receiver);
        model.addAttribute("messages", messages);
        model.addAttribute("user", sender);
        return "common/chat";
    }

    @PostMapping("/send")
    public String sendMessage(@RequestParam("receiverId") Long receiverId,
                              @RequestParam("content") String content,
                              HttpSession session) {
        User sender = (User) session.getAttribute("loggedUser");
        if (sender == null) return "redirect:/auth/login";
        User receiver = userRepository.findById(receiverId).orElse(null);
        if (receiver == null) return "redirect:/";
        Message msg = new Message();
        msg.setSender(sender);
        msg.setReceiver(receiver);
        msg.setMessageContent(content);
        msg.setSentAt(LocalDateTime.now());
        msg.setRead(false);
        messageRepository.save(msg);
        return "redirect:/messages?userId=" + receiverId;
    }

    @GetMapping("/application/{applicationId}")
    public String applicationChat(@PathVariable Long applicationId, HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) return "redirect:/auth/login";

        Application application = applicationRepository.findById(applicationId.intValue()).orElse(null);
        if (application == null) return "redirect:/";

        boolean isOwner = user.getId().equals(application.getApplicant().getId()) ||
                user.getId().equals(application.getJob().getRecruiter().getUser().getId());

        if (!isOwner) return "redirect:/";

        List<Message> messages = messageRepository
                .findByApplicationIdOrderBySentAt(application.getId().longValue());

        User receiver = user.getRole().name().equals("APPLICANT")
                ? application.getJob().getRecruiter().getUser()
                : application.getApplicant();

        model.addAttribute("application", application);
        model.addAttribute("receiver", receiver);
        model.addAttribute("messages", messages);
        model.addAttribute("user", user);
        return "common/application-chat";
    }

    @PostMapping("/application/{applicationId}/send")
    public String sendAppMessage(@PathVariable Long applicationId,
                                 @RequestParam("content") String content,
                                 HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) return "redirect:/auth/login";

        Application application = applicationRepository.findById(applicationId.intValue()).orElse(null);
        if (application == null) return "redirect:/";

        boolean isOwner = user.getId().equals(application.getApplicant().getId()) ||
                user.getId().equals(application.getJob().getRecruiter().getUser().getId());

        if (!isOwner) return "redirect:/";

        User receiver = user.getRole().name().equals("APPLICANT")
                ? application.getJob().getRecruiter().getUser()
                : application.getApplicant();

        Message msg = new Message();
        msg.setSender(user);
        msg.setReceiver(receiver);
        msg.setMessageContent(content);
        msg.setSentAt(LocalDateTime.now());
        msg.setRead(false);
        msg.setApplication(application);
        messageRepository.save(msg);

        return "redirect:/messages/application/" + applicationId;
    }

    @ModelAttribute("activeChatApplicationId")
    public Long loadActiveChat(HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) return null;

        Optional<Message> lastMsg = messageRepository
                .findFirstBySenderIdOrReceiverIdOrderBySentAtDesc(user.getId(), user.getId());

        return lastMsg
                .map(Message::getApplication)
                .map(app -> app.getId().longValue())
                .orElse(null);
    }
}
