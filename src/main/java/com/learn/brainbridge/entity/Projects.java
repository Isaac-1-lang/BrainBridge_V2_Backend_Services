package com.learn.brainbridge.entity;

import jakarta.persistence.*;
import com.learn.brainbridge.enums.ProjectStatus;
import com.learn.brainbridge.enums.ProjectVisibility;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(
    name = "Projects",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_owner_title", columnNames = {"owner_id", "title"})
    }
)
public class Projects {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "projects_seq")
    @SequenceGenerator(name = "projects_seq", sequenceName = "projects_seq", allocationSize = 50)
    private Integer id;

    @Column(nullable = false, name = "title")
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "project_status")
    private ProjectStatus projectStatus;

    @Column(nullable = false, name = "project_visibility")
    @Enumerated(EnumType.STRING)
    private ProjectVisibility projectVisibility;

    @Column(nullable = false, name = "owner_id")
    private Integer ownerId;

    @Column(nullable = true, name = "team_id")
    private Integer teamId;

    @Column(nullable = true, name = "source_idea_id")
    private Integer sourceIdeaId;

    @Column(nullable = true, columnDefinition = "TEXT", name = "cover_image_url")
    private String coverImageUrl;

    @Column(nullable = true, columnDefinition = "TEXT", name = "repo_url")
    private String repoUrl;

    @Column(nullable = false, name = "start_date")
    private LocalDate startDate;

    @Column(nullable = false, name = "end_date")
    private LocalDate endDate;

    @Column(nullable = false, updatable = false, name = "created_at")
    @CreatedDate
    private LocalDate createdAt;

    @LastModifiedDate
    @Column(nullable = false, name = "updated_at")
    private LocalDate updatedAt;

    @Column(nullable = false, name = "view_count", columnDefinition = "integer default 0")
    private Integer viewCount = 0;

    @Column(nullable = false, name = "enterprise_requests", columnDefinition = "integer default 0")
    private Integer enterpriseRequests = 0;

    @Column(name = "project_field")
    private String field;

    @ElementCollection
    @CollectionTable(name = "project_main_tags", joinColumns = @JoinColumn(name = "project_id"))
    @Column(name = "tag")
    private List<String> mainTags = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "project_sub_tags", joinColumns = @JoinColumn(name = "project_id"))
    @Column(name = "tag")
    private List<String> subTags = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "project_sdg_goals", joinColumns = @JoinColumn(name = "project_id"))
    @Column(name = "goal")
    private List<String> sdgGoals = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "project_nst2_goals", joinColumns = @JoinColumn(name = "project_id"))
    @Column(name = "goal")
    private List<String> nst2Goals = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "project_additional_media", joinColumns = @JoinColumn(name = "project_id"))
    @Column(name = "media_url", columnDefinition = "TEXT")
    private List<String> additionalMediaUrls = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDate.now();
        }
        if (updatedAt == null) {
            updatedAt = LocalDate.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDate.now();
    }

    public Projects() {
    }

    public Projects(String title, String description, ProjectStatus projectStatus,
            ProjectVisibility projectVisibility, Integer ownerId, Integer teamId,
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

    public ProjectStatus getProjectStatus() {
        return projectStatus;
    }

    public ProjectVisibility getProjectVisibility() {
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

    public void setProjectStatus(ProjectStatus projectStatus) {
        this.projectStatus = projectStatus;
    }

    public void setProjectVisibility(ProjectVisibility projectVisibility) {
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

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public Integer getEnterpriseRequests() {
        return enterpriseRequests;
    }

    public void setEnterpriseRequests(Integer enterpriseRequests) {
        this.enterpriseRequests = enterpriseRequests;
    }

    public String getField() { return field; }
    public void setField(String field) { this.field = field; }

    public List<String> getMainTags() { return mainTags; }
    public void setMainTags(List<String> mainTags) { this.mainTags = mainTags; }

    public List<String> getSubTags() { return subTags; }
    public void setSubTags(List<String> subTags) { this.subTags = subTags; }

    public List<String> getSdgGoals() { return sdgGoals; }
    public void setSdgGoals(List<String> sdgGoals) { this.sdgGoals = sdgGoals; }

    public List<String> getNst2Goals() { return nst2Goals; }
    public void setNst2Goals(List<String> nst2Goals) { this.nst2Goals = nst2Goals; }

    public List<String> getAdditionalMediaUrls() { return additionalMediaUrls; }
    public void setAdditionalMediaUrls(List<String> additionalMediaUrls) { this.additionalMediaUrls = additionalMediaUrls; }
}