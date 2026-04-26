package com.learn.brainbridge.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.learn.brainbridge.enums.ProjectMemberRole;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

public class InvitationsDTO {
    @JsonProperty("id")
    private Integer id;

    @JsonProperty("projectId")
    @NotNull(message = "Project ID is required")
    private Integer projectId;

    @JsonProperty("invitedUserId")
    @NotNull(message = "Invited user ID is required")
    private Integer invitedUserId;

    @JsonProperty("invitedByUserId")
    private Integer invitedByUserId;

    @JsonProperty("role")
    @NotNull(message = "Role is required")
    private ProjectMemberRole role;

    @JsonProperty("status")
    private String status;

    @JsonProperty("createdAt")
    private LocalDate createdAt;

    @JsonProperty("respondedAt")
    private LocalDate respondedAt;

    @JsonProperty("message")
    private String message;

    // Constructors
    public InvitationsDTO() {}

    public InvitationsDTO(Integer projectId, Integer invitedUserId, ProjectMemberRole role, String message) {
        this.projectId = projectId;
        this.invitedUserId = invitedUserId;
        this.role = role;
        this.message = message;
    }

    public InvitationsDTO(Integer id, Integer projectId, Integer invitedUserId, Integer invitedByUserId,
            ProjectMemberRole role, String status, LocalDate createdAt, LocalDate respondedAt, String message) {
        this.id = id;
        this.projectId = projectId;
        this.invitedUserId = invitedUserId;
        this.invitedByUserId = invitedByUserId;
        this.role = role;
        this.status = status;
        this.createdAt = createdAt;
        this.respondedAt = respondedAt;
        this.message = message;
    }

    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getProjectId() { return projectId; }
    public void setProjectId(Integer projectId) { this.projectId = projectId; }

    public Integer getInvitedUserId() { return invitedUserId; }
    public void setInvitedUserId(Integer invitedUserId) { this.invitedUserId = invitedUserId; }

    public Integer getInvitedByUserId() { return invitedByUserId; }
    public void setInvitedByUserId(Integer invitedByUserId) { this.invitedByUserId = invitedByUserId; }

    public ProjectMemberRole getRole() { return role; }
    public void setRole(ProjectMemberRole role) { this.role = role; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDate getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDate createdAt) { this.createdAt = createdAt; }

    public LocalDate getRespondedAt() { return respondedAt; }
    public void setRespondedAt(LocalDate respondedAt) { this.respondedAt = respondedAt; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
