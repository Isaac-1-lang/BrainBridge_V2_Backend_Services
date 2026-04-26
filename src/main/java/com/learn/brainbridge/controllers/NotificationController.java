package com.learn.brainbridge.controllers;

import com.learn.brainbridge.dtos.NotificationDTO;
import com.learn.brainbridge.entity.User;
import com.learn.brainbridge.repository.UserRepository;
import com.learn.brainbridge.service.NotificationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@Tag(name = "Notifications API", description = "User notification operations")
@RequestMapping("/notifications")
public class NotificationController {

    private static final Logger logger = LoggerFactory.getLogger(NotificationController.class);
    private final NotificationService notificationService;
    private final UserRepository userRepository;

    @Autowired
    public NotificationController(NotificationService notificationService, UserRepository userRepository) {
        this.notificationService = notificationService;
        this.userRepository = userRepository;
    }

    @GetMapping
    @Transactional(readOnly = true)
    public ResponseEntity<?> getNotifications(Authentication authentication) {
        try {
            User user = getCurrentUser(authentication);
            if (user == null)
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");

            List<NotificationDTO> notifications = notificationService.getNotificationsForUser(user).stream()
                    .map(n -> new NotificationDTO(n.getId(), n.getType(), n.getTitle(), n.getBody(), n.getIsRead(),
                            n.getCreatedAt()))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(notifications);
        } catch (Exception e) {
            logger.error("Failed to get notifications", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/unread-count")
    @Transactional(readOnly = true)
    public ResponseEntity<?> getUnreadCount(Authentication authentication) {
        try {
            User user = getCurrentUser(authentication);
            if (user == null)
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");

            long count = notificationService.getUnreadCount(user);
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            logger.error("Failed to get unread count", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/read/{id}")
    public ResponseEntity<?> markAsRead(@PathVariable("id") Long id) {
        notificationService.markAsRead(id);
        return ResponseEntity.ok().build();
    }

    private User getCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated())
            return null;
        String name = authentication.getName();
        return userRepository.findByEmail(name)
                .orElseGet(() -> userRepository.findByUsername(name).orElse(null));
    }
}
