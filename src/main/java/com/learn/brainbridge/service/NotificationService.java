package com.learn.brainbridge.service;

import com.learn.brainbridge.entity.Notification;
import com.learn.brainbridge.entity.User;
import com.learn.brainbridge.enums.NotificationType;
import com.learn.brainbridge.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import com.learn.brainbridge.dtos.NotificationDTO;

import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository repo;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public NotificationService(NotificationRepository repo, SimpMessagingTemplate messagingTemplate) {
        this.repo = repo;
        this.messagingTemplate = messagingTemplate;
    }

    public void createNotification(User user, NotificationType type, String title, String body) {
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setType(type);
        notification.setTitle(title);
        notification.setBody(body);
        notification.setIsRead(false);
        Notification saved = repo.save(notification);

        NotificationDTO dto = new NotificationDTO(saved.getId(), saved.getType(),
                saved.getTitle(), saved.getBody(), saved.getIsRead(), saved.getCreatedAt());
        
        // Use Email because JWT subject is Email
        messagingTemplate.convertAndSendToUser(user.getEmail(), "/queue/notifications", dto);
    }

    public List<Notification> getNotificationsForUser(User user) {
        return repo.findByUserOrderByCreatedAtDesc(user);
    }

    public long getUnreadCount(User user) {
        return repo.countByUserAndIsReadFalse(user);
    }

    public void markAsRead(Long notificationId) {
        repo.findById(notificationId).ifPresent(n -> {
            n.setIsRead(true);
            repo.save(n);
        });
    }
}
