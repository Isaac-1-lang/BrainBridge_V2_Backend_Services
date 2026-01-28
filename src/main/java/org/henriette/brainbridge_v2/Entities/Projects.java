package org.henriette.brainbridge_v2.Entities;

import jakarta.persistence.*;
import org.henriette.brainbridge_v2.Enums.Status;
import org.henriette.brainbridge_v2.Enums.Visibility;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "Projects")
public class Projects {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "projects_seq")
    @SequenceGenerator(name="projects_seq",sequenceName = "projects_seq",allocationSize=50)
    private Integer id;

    @Column(nullable = false,unique = true,name = "title")
    private String title;

    @Column(nullable = false,columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false,name = "project_status")
    private Status projectStatus;

    @Column(nullable = false,name = "project_visibility")
    @Enumerated(EnumType.STRING)
    private Visibility projectVisibility;

    @Column(nullable = false,name = "owner_id")
    private Integer ownerId;

    @Column(nullable = false,name = "team_id")
    private Integer teamId;

    @Column(nullable = false,name = "source_idea_id")
    private Integer sourceIdeaId;

    @Column(nullable = false,columnDefinition = "TEXT",name = "cover_image_url")
    private String coverImageUrl;

    @Column(nullable = false,columnDefinition = "TEXT",name = "repo_url")
    private String repoUrl;

    @Column(nullable = false,name = "start_date")
    private LocalDate startDate;

    @Column(nullable = false,name = "end_date")
    private LocalDate endDate;

    @Column(nullable = false,updatable = false,name = "created_at")
    @CreatedDate
    private LocalDate createdAt;

    @LastModifiedDate
    @Column(nullable = false,name = "updated_at")
    private LocalDate updatedAt;

    public Projects() {
    }

    public Projects(String title, String description, Status projectStatus,
                    Visibility projectVisibility, Integer ownerId, Integer teamId,
                    Integer sourceIdeaId, String coverImageUrl, String repoUrl,
                    LocalDate startDate, LocalDate endDate) {
        this.title = title;
        this.description = description;
        this.projectStatus = projectStatus;
        this.projectVisibility = projectVisibility;
        this.ownerId = ownerId;
        this.teamId = teamId;
        this.sourceIdeaId = sourceIdeaId;
        this.coverImageUrl = coverImageUrl;
        this.repoUrl = repoUrl;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Status getProjectStatus() {
        return projectStatus;
    }

    public Visibility getProjectVisibility() {
        return projectVisibility;
    }

    public Integer getOwnerId() {
        return ownerId;
    }

    public Integer getTeamId() {
        return teamId;
    }

    public Integer getSourceIdeaId() {
        return sourceIdeaId;
    }

    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    public String getRepoUrl() {
        return repoUrl;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public LocalDate getUpdatedAt() {
        return updatedAt;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setProjectStatus(Status projectStatus) {
        this.projectStatus = projectStatus;
    }

    public void setProjectVisibility(Visibility projectVisibility) {
        this.projectVisibility = projectVisibility;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }

    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
    }

    public void setSourceIdeaId(Integer sourceIdeaId) {
        this.sourceIdeaId = sourceIdeaId;
    }

    public void setCoverImageUrl(String coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
    }

    public void setRepoUrl(String repoUrl) {
        this.repoUrl = repoUrl;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
