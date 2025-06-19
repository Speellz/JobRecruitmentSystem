package com.jobrecruitment.service.common;

import com.jobrecruitment.model.User;
import com.jobrecruitment.model.common.UserNotification;
import com.jobrecruitment.repository.common.UserNotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserNotificationService {

    private final UserNotificationRepository notificationRepository;

    public List<UserNotification> getNotificationsByUserId(Long userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public void createNotification(UserNotification notification) {
        notificationRepository.save(notification);
    }

    public void deleteNotification(Long id) {
        notificationRepository.deleteById(id);
    }

    public void deleteAllByUserId(Long userId) {
        notificationRepository.deleteByUserId(userId);
    }

    public void sendNotification(User user, String message, String link) {
        UserNotification notification = new UserNotification();
        notification.setUser(user);
        notification.setMessage(message);
        notification.setLink(link);
        notification.setCreatedAt(LocalDateTime.now());
        notificationRepository.save(notification);
    }
}
