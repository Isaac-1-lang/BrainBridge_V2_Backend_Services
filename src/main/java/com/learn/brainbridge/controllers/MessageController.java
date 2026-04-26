package com.learn.brainbridge.controllers;

import com.learn.brainbridge.dtos.MessageDTO;
import com.learn.brainbridge.entity.Message;
import com.learn.brainbridge.entity.Projects;
import com.learn.brainbridge.entity.User;
import com.learn.brainbridge.repository.ProjectsRepository;
import com.learn.brainbridge.repository.UserRepository;
import com.learn.brainbridge.service.MessageService;
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
@Tag(name = "Messages API", description = "Messaging and collaboration log operations")
@RequestMapping("/messages")
public class MessageController {

    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);
    private final MessageService messageService;
    private final UserRepository userRepository;
    private final ProjectsRepository projectsRepository;

    @Autowired
    public MessageController(MessageService messageService, UserRepository userRepository,
            ProjectsRepository projectsRepository) {
        this.messageService = messageService;
        this.userRepository = userRepository;
        this.projectsRepository = projectsRepository;
    }

    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(@RequestBody MessageDTO dto, Authentication authentication) {
        try {
            logger.info("Sending message: sender={}, receiver={}, project={}",
                    authentication != null ? authentication.getName() : "null", dto.getReceiverId(),
                    dto.getProjectId());
            User sender = getCurrentUser(authentication);
            if (sender == null)
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");

            User receiver = userRepository.findById(dto.getReceiverId()).orElse(null);
            if (receiver == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Receiver not found");

            Projects project = null;
            if (dto.getProjectId() != null) {
                project = projectsRepository.findById(dto.getProjectId()).orElse(null);
            }

            Message saved = messageService.sendMessage(sender, receiver, project, dto.getContent());
            return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(saved));
        } catch (Exception e) {
            logger.error("Failed to send message", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/inbox")
    @Transactional(readOnly = true)
    public ResponseEntity<?> getInbox(Authentication authentication) {
        try {
            User user = getCurrentUser(authentication);
            if (user == null)
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");

            List<MessageDTO> inbox = messageService.getInbox(user).stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(inbox);
        } catch (Exception e) {
            logger.error("Failed to get inbox", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/project/{id}")
    @Transactional(readOnly = true)
    public ResponseEntity<?> getProjectLog(@PathVariable("id") Integer id) {
        try {
            Projects project = projectsRepository.findById(id).orElse(null);
            if (project == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Project not found");

            List<MessageDTO> log = messageService.getProjectCollaborationLog(project).stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(log);
        } catch (Exception e) {
            logger.error("Failed to get project log", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    private User getCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated())
            return null;
        String name = authentication.getName();
        return userRepository.findByEmail(name)
                .orElseGet(() -> userRepository.findByUsername(name).orElse(null));
    }

    private MessageDTO convertToDTO(Message m) {
        if (m == null)
            return null;
        MessageDTO dto = new MessageDTO();
        dto.setId(m.getId());

        if (m.getSender() != null) {
            dto.setSenderId(m.getSender().getId());
            dto.setSenderUsername(m.getSender().getUsername());
        }

        if (m.getReceiver() != null) {
            dto.setReceiverId(m.getReceiver().getId());
            dto.setReceiverUsername(m.getReceiver().getUsername());
        }

        if (m.getProject() != null) {
            dto.setProjectId(m.getProject().getId());
            dto.setProjectName(m.getProject().getTitle());
        }

        dto.setContent(m.getContent());
        dto.setIsRead(m.getIsRead());
        dto.setCreatedAt(m.getCreatedAt());
        return dto;
    }
}
