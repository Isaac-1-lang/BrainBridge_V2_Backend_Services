package com.learn.brainbridge.entity;

import jakarta.persistence.*;
import com.learn.brainbridge.enums.ProjectMemberRole;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(
    name = "Invitations",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_project_user_invitation", columnNames = {"project_id", "invited_user_id"})
    }
)
public class Invitations {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "invitations_seq")
    @SequenceGenerator(name = "invitations_seq", sequenceName = "invitations_seq", allocationSize = 50)
    private Integer id;

    @Column(nullable = false, name = "project_id")
    private Integer projectId;

    @Column(nullable = false, name = "invited_user_id")
    private Integer invitedUserId;

    @Column(nullable = false, name = "invited_by_user_id")
    private Integer invitedByUserId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "role")
    private ProjectMemberRole role;

    @Column(nullable = false, name = "status")
    private String status = "PENDING"; // PENDING, ACCEPTED, REJECTED, REVOKED

    @Column(nullable = false, updatable = false, name = "created_at")
    @CreatedDate
    private LocalDate createdAt;

    @Column(nullable = true, name = "responded_at")
    private LocalDate respondedAt;

    @Column(nullable = true, columnDefinition = "TEXT", name = "message")
    private String message;

    // Constructors
    public Invitations() {}

    public Invitations(Integer projectId, Integer invitedUserId, Integer invitedByUserId,
            ProjectMemberRole role, String message) {
        this.projectId = projectId;
        this.invitedUserId = invitedUserId;
        this.invitedByUserId = invitedByUserId;
        this.role = role;
        this.message = message;
        this.status = "PENDING";
    }

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDate.now();
        }
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
