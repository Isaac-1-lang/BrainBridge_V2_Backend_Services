package com.learn.brainbridge.controllers;

import com.learn.brainbridge.dtos.InvitationsDTO;
import com.learn.brainbridge.entity.Invitations;
import com.learn.brainbridge.entity.Projects;
import com.learn.brainbridge.entity.User;
import com.learn.brainbridge.repository.ProjectsRepository;
import com.learn.brainbridge.repository.UserRepository;
import com.learn.brainbridge.service.InvitationsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@Tag(name = "Invitations API", description = "Project collaboration invitations management")
@RequestMapping("/invitations")
public class InvitationsController {
    private static final Logger log = LoggerFactory.getLogger(InvitationsController.class);
    private final InvitationsService invitationsService;
    private final ProjectsRepository projectsRepository;
    private final UserRepository userRepository;

    @Autowired
    public InvitationsController(InvitationsService invitationsService, ProjectsRepository projectsRepository,
            UserRepository userRepository) {
        this.invitationsService = invitationsService;
        this.projectsRepository = projectsRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/send")
    public ResponseEntity<?> sendInvitation(Authentication authentication, @Valid @RequestBody InvitationsDTO invitationDTO) {
        log.info("=== Send Invitation Request ===");
        log.info("  Project ID: {}", invitationDTO.getProjectId());
        log.info("  Invited User ID: {}", invitationDTO.getInvitedUserId());
        log.info("  Role: {}", invitationDTO.getRole());

        // Verify authentication
        if (authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getName())) {
            log.warn("  REJECTED: No valid authentication");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Unauthorized"));
        }

        // Get current user
        String principalName = authentication.getName();
        User currentUser = userRepository.findByEmail(principalName)
                .orElseGet(() -> userRepository.findByUsername(principalName).orElse(null));

        if (currentUser == null) {
            log.warn("  REJECTED: Current user not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        // Verify project exists and user is owner
        Optional<Projects> projectOpt = projectsRepository.findById(invitationDTO.getProjectId());
        if (projectOpt.isEmpty()) {
            log.warn("  REJECTED: Project not found: {}", invitationDTO.getProjectId());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Project not found");
        }

        Projects project = projectOpt.get();
        Integer currentUserId = Math.toIntExact(currentUser.getId());

        if (!project.getOwnerId().equals(currentUserId)) {
            log.warn("  REJECTED: User {} is not project owner", currentUserId);
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Only project owner can send invitations"));
        }

        // Check if user is already invited
        if (invitationsService.isUserInvited(invitationDTO.getProjectId(), invitationDTO.getInvitedUserId())) {
            log.warn("  REJECTED: User already invited to project");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "User is already invited to this project"));
        }

        // Verify invited user exists
        Optional<User> invitedUserOpt = userRepository.findById((long) invitationDTO.getInvitedUserId());
        if (invitedUserOpt.isEmpty()) {
            log.warn("  REJECTED: Invited user not found: {}", invitationDTO.getInvitedUserId());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invited user not found");
        }

        // Create and save invitation
        Invitations invitation = new Invitations(
            invitationDTO.getProjectId(),
            invitationDTO.getInvitedUserId(),
            currentUserId,
            invitationDTO.getRole(),
            invitationDTO.getMessage()
        );

        Invitations saved = invitationsService.sendInvitation(invitation);
        log.info("  Invitation sent successfully. ID: {}", saved.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(invitationsService.convertToDTO(saved));
    }

    @GetMapping("/pending")
    @Transactional(readOnly = true)
    public ResponseEntity<?> getPendingInvitations(Authentication authentication) {
        log.info("=== Get Pending Invitations Request ===");

        if (authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getName())) {
            log.warn("  REJECTED: No valid authentication");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Unauthorized"));
        }

        String principalName = authentication.getName();
        User user = userRepository.findByEmail(principalName)
                .orElseGet(() -> userRepository.findByUsername(principalName).orElse(null));

        if (user == null) {
            log.warn("  REJECTED: User not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        Integer userId = Math.toIntExact(user.getId());
        List<Invitations> invitations = invitationsService.getUserPendingInvitations(userId);
        List<InvitationsDTO> dtos = invitations.stream()
            .map(invitationsService::convertToDTO)
            .collect(Collectors.toList());

        log.info("  Found {} pending invitations", dtos.size());
        return ResponseEntity.status(HttpStatus.OK).body(dtos);
    }

    @PostMapping("/accept/{invitationId}")
    public ResponseEntity<?> acceptInvitation(Authentication authentication, @PathVariable Integer invitationId) {
        log.info("=== Accept Invitation Request ===");
        log.info("  Invitation ID: {}", invitationId);

        if (authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getName())) {
            log.warn("  REJECTED: No valid authentication");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Unauthorized"));
        }

        String principalName = authentication.getName();
        User user = userRepository.findByEmail(principalName)
                .orElseGet(() -> userRepository.findByUsername(principalName).orElse(null));

        if (user == null) {
            log.warn("  REJECTED: User not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        Optional<Invitations> invitationOpt = invitationsService.getInvitationById(invitationId);
        if (invitationOpt.isEmpty()) {
            log.warn("  REJECTED: Invitation not found: {}", invitationId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invitation not found");
        }

        Invitations invitation = invitationOpt.get();
        Integer userId = Math.toIntExact(user.getId());

        if (!invitation.getInvitedUserId().equals(userId)) {
            log.warn("  REJECTED: User {} is not the invited user", userId);
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "You cannot accept this invitation"));
        }

        if (!invitation.getStatus().equals("PENDING")) {
            log.warn("  REJECTED: Invitation is not pending. Status: {}", invitation.getStatus());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Invitation is not pending"));
        }

        Invitations accepted = invitationsService.acceptInvitation(invitationId);
        log.info("  Invitation accepted successfully");

        return ResponseEntity.status(HttpStatus.OK).body(invitationsService.convertToDTO(accepted));
    }

    @PostMapping("/reject/{invitationId}")
    public ResponseEntity<?> rejectInvitation(Authentication authentication, @PathVariable Integer invitationId) {
        log.info("=== Reject Invitation Request ===");
        log.info("  Invitation ID: {}", invitationId);

        if (authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getName())) {
            log.warn("  REJECTED: No valid authentication");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Unauthorized"));
        }

        String principalName = authentication.getName();
        User user = userRepository.findByEmail(principalName)
                .orElseGet(() -> userRepository.findByUsername(principalName).orElse(null));

        if (user == null) {
            log.warn("  REJECTED: User not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        Optional<Invitations> invitationOpt = invitationsService.getInvitationById(invitationId);
        if (invitationOpt.isEmpty()) {
            log.warn("  REJECTED: Invitation not found: {}", invitationId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invitation not found");
        }

        Invitations invitation = invitationOpt.get();
        Integer userId = Math.toIntExact(user.getId());

        if (!invitation.getInvitedUserId().equals(userId)) {
            log.warn("  REJECTED: User {} is not the invited user", userId);
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "You cannot reject this invitation"));
        }

        Invitations rejected = invitationsService.rejectInvitation(invitationId);
        log.info("  Invitation rejected successfully");

        return ResponseEntity.status(HttpStatus.OK).body(invitationsService.convertToDTO(rejected));
    }

    @PostMapping("/revoke/{invitationId}")
    public ResponseEntity<?> revokeInvitation(Authentication authentication, @PathVariable Integer invitationId) {
        log.info("=== Revoke Invitation Request ===");
        log.info("  Invitation ID: {}", invitationId);

        if (authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getName())) {
            log.warn("  REJECTED: No valid authentication");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Unauthorized"));
        }

        String principalName = authentication.getName();
        User user = userRepository.findByEmail(principalName)
                .orElseGet(() -> userRepository.findByUsername(principalName).orElse(null));

        if (user == null) {
            log.warn("  REJECTED: User not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        Optional<Invitations> invitationOpt = invitationsService.getInvitationById(invitationId);
        if (invitationOpt.isEmpty()) {
            log.warn("  REJECTED: Invitation not found: {}", invitationId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invitation not found");
        }

        Invitations invitation = invitationOpt.get();
        Optional<Projects> projectOpt = projectsRepository.findById(invitation.getProjectId());

        if (projectOpt.isEmpty()) {
            log.warn("  REJECTED: Project not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Project not found");
        }

        Projects project = projectOpt.get();
        Integer userId = Math.toIntExact(user.getId());

        if (!project.getOwnerId().equals(userId)) {
            log.warn("  REJECTED: User {} is not project owner", userId);
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Only project owner can revoke invitations"));
        }

        Invitations revoked = invitationsService.revokeInvitation(invitationId);
        log.info("  Invitation revoked successfully");

        return ResponseEntity.status(HttpStatus.OK).body(invitationsService.convertToDTO(revoked));
    }

    @GetMapping("/project/{projectId}")
    @Transactional(readOnly = true)
    public ResponseEntity<?> getProjectInvitations(Authentication authentication, @PathVariable Integer projectId) {
        log.info("=== Get Project Invitations Request ===");
        log.info("  Project ID: {}", projectId);

        if (authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getName())) {
            log.warn("  REJECTED: No valid authentication");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Unauthorized"));
        }

        String principalName = authentication.getName();
        User user = userRepository.findByEmail(principalName)
                .orElseGet(() -> userRepository.findByUsername(principalName).orElse(null));

        if (user == null) {
            log.warn("  REJECTED: User not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        Optional<Projects> projectOpt = projectsRepository.findById(projectId);
        if (projectOpt.isEmpty()) {
            log.warn("  REJECTED: Project not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Project not found");
        }

        Projects project = projectOpt.get();
        Integer userId = Math.toIntExact(user.getId());

        if (!project.getOwnerId().equals(userId)) {
            log.warn("  REJECTED: User {} is not project owner", userId);
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Only project owner can view invitations"));
        }

        List<Invitations> invitations = invitationsService.getProjectInvitations(projectId);
        List<InvitationsDTO> dtos = invitations.stream()
            .map(invitationsService::convertToDTO)
            .collect(Collectors.toList());

        log.info("  Found {} invitations for project", dtos.size());
        return ResponseEntity.status(HttpStatus.OK).body(dtos);
    }

    @GetMapping("/project/{projectId}/members")
    @Transactional(readOnly = true)
    public ResponseEntity<?> getProjectMembers(@PathVariable Integer projectId) {
        log.info("=== Get Project Members Request ===");
        log.info("  Project ID: {}", projectId);

        Optional<Projects> projectOpt = projectsRepository.findById(projectId);
        if (projectOpt.isEmpty()) {
            log.warn("  REJECTED: Project not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Project not found");
        }

        List<Invitations> members = invitationsService.getProjectMembers(projectId);
        List<InvitationsDTO> dtos = members.stream()
            .map(invitationsService::convertToDTO)
            .collect(Collectors.toList());

        log.info("  Found {} members for project", dtos.size());
        return ResponseEntity.status(HttpStatus.OK).body(dtos);
    }
}
