package com.learn.brainbridge.service;

import com.learn.brainbridge.entity.Message;
import com.learn.brainbridge.entity.User;
import com.learn.brainbridge.entity.Projects;
import com.learn.brainbridge.enums.NotificationType;
import com.learn.brainbridge.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import com.learn.brainbridge.dtos.MessageDTO;

import java.util.List;

@Service
public class MessageService {

    private final MessageRepository messageRepo;
    private final NotificationService notificationService;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public MessageService(MessageRepository messageRepo, NotificationService notificationService, SimpMessagingTemplate messagingTemplate) {
        this.messageRepo = messageRepo;
        this.notificationService = notificationService;
        this.messagingTemplate = messagingTemplate;
    }

    public Message sendMessage(User sender, User receiver, Projects project, String content) {
        Message message = new Message();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setProject(project);
        message.setContent(content);
        message.setIsRead(false);
        Message saved = messageRepo.save(message);

        MessageDTO dto = new MessageDTO();
        dto.setId(saved.getId());
        dto.setSenderId(sender.getId());
        dto.setSenderUsername(sender.getUsername());
        dto.setReceiverId(receiver.getId());
        dto.setReceiverUsername(receiver.getUsername());
        if (project != null) {
            dto.setProjectId(project.getId());
            dto.setProjectName(project.getTitle());
        }
        dto.setContent(saved.getContent());
        dto.setIsRead(saved.getIsRead());
        dto.setCreatedAt(saved.getCreatedAt());

        // WebSocket connections use Email as the principal identity (extracted from JWT)
        messagingTemplate.convertAndSendToUser(receiver.getEmail(), "/queue/messages", dto);

        // Notify the receiver
        String title = (project != null) ? "Collaboration: " + project.getTitle() : "Direct Message";
        String body = sender.getUsername() + " sent you a message: " +
                (content.length() > 50 ? content.substring(0, 47) + "..." : content);

        notificationService.createNotification(receiver, NotificationType.COLLABORATION, title, body);

        return saved;
    }

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public List<Message> getInbox(User user) {
        return messageRepo.findUserMessages(user);
    }

    public List<Message> getProjectCollaborationLog(Projects project) {
        return messageRepo.findByProjectOrderByCreatedAtAsc(project);
    }

    public void markAsRead(Long messageId) {
        messageRepo.findById(messageId).ifPresent(m -> {
            m.setIsRead(true);
            messageRepo.save(m);
        });
    }
}
