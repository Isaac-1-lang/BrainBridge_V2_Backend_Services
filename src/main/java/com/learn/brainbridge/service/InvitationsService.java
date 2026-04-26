package com.learn.brainbridge.service;

import com.learn.brainbridge.dtos.InvitationsDTO;
import com.learn.brainbridge.entity.Invitations;
import com.learn.brainbridge.entity.Projects;
import com.learn.brainbridge.repository.InvitationsRepository;
import com.learn.brainbridge.repository.ProjectsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InvitationsService {

    private final InvitationsRepository invitationsRepo;
    private final ProjectsRepository projectsRepo;

    @Autowired
    public InvitationsService(InvitationsRepository invitationsRepo, ProjectsRepository projectsRepo) {
        this.invitationsRepo = invitationsRepo;
        this.projectsRepo = projectsRepo;
    }

    // Send invitation
    public Invitations sendInvitation(Invitations invitation) {
        return invitationsRepo.save(invitation);
    }

    // Get all invitations for a project
    public List<Invitations> getProjectInvitations(Integer projectId) {
        return invitationsRepo.findByProjectId(projectId);
    }

    // Get pending invitations for a user
    public List<Invitations> getUserPendingInvitations(Integer userId) {
        return invitationsRepo.findByInvitedUserIdAndStatus(userId, "PENDING");
    }

    // Get all invitations for a user (any status)
    public List<Invitations> getUserInvitations(Integer userId) {
        return invitationsRepo.findByInvitedUserId(userId);
    }

    // Accept invitation
    public Invitations acceptInvitation(Integer invitationId) {
        Optional<Invitations> invitationOpt = invitationsRepo.findById(invitationId);
        if (invitationOpt.isEmpty()) {
            return null;
        }

        Invitations invitation = invitationOpt.get();
        invitation.setStatus("ACCEPTED");
        invitation.setRespondedAt(LocalDate.now());
        return invitationsRepo.save(invitation);
    }

    // Reject invitation
    public Invitations rejectInvitation(Integer invitationId) {
        Optional<Invitations> invitationOpt = invitationsRepo.findById(invitationId);
        if (invitationOpt.isEmpty()) {
            return null;
        }

        Invitations invitation = invitationOpt.get();
        invitation.setStatus("REJECTED");
        invitation.setRespondedAt(LocalDate.now());
        return invitationsRepo.save(invitation);
    }

    // Revoke invitation (by project owner)
    public Invitations revokeInvitation(Integer invitationId) {
        Optional<Invitations> invitationOpt = invitationsRepo.findById(invitationId);
        if (invitationOpt.isEmpty()) {
            return null;
        }

        Invitations invitation = invitationOpt.get();
        invitation.setStatus("REVOKED");
        invitation.setRespondedAt(LocalDate.now());
        return invitationsRepo.save(invitation);
    }

    // Check if user is already invited to project
    public boolean isUserInvited(Integer projectId, Integer userId) {
        return invitationsRepo.findByProjectIdAndInvitedUserId(projectId, userId).isPresent();
    }

    // Get invitation by ID
    public Optional<Invitations> getInvitationById(Integer id) {
        return invitationsRepo.findById(id);
    }

    // Delete invitation
    public void deleteInvitation(Integer invitationId) {
        invitationsRepo.deleteById(invitationId);
    }

    // Get pending invitations for a project
    public List<Invitations> getProjectPendingInvitations(Integer projectId) {
        return invitationsRepo.findByProjectIdAndStatus(projectId, "PENDING");
    }

    // Get accepted members of a project
    public List<Invitations> getProjectMembers(Integer projectId) {
        return invitationsRepo.findByProjectIdAndStatus(projectId, "ACCEPTED");
    }

    // Convert entity to DTO
    public InvitationsDTO convertToDTO(Invitations invitation) {
        return new InvitationsDTO(
            invitation.getId(),
            invitation.getProjectId(),
            invitation.getInvitedUserId(),
            invitation.getInvitedByUserId(),
            invitation.getRole(),
            invitation.getStatus(),
            invitation.getCreatedAt(),
            invitation.getRespondedAt(),
            invitation.getMessage()
        );
    }

    // Convert DTO to entity
    public Invitations convertToEntity(InvitationsDTO dto) {
        Invitations invitation = new Invitations();
        invitation.setProjectId(dto.getProjectId());
        invitation.setInvitedUserId(dto.getInvitedUserId());
        invitation.setInvitedByUserId(dto.getInvitedByUserId());
        invitation.setRole(dto.getRole());
        invitation.setStatus(dto.getStatus() != null ? dto.getStatus() : "PENDING");
        invitation.setMessage(dto.getMessage());
        return invitation;
    }
}
