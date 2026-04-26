package com.learn.brainbridge.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;

public class AnalyticsDTO {
    @JsonProperty("projectId")
    private Integer projectId;

    @JsonProperty("projectTitle")
    private String projectTitle;

    @JsonProperty("viewCount")
    private Integer viewCount;

    @JsonProperty("enterpriseRequests")
    private Integer enterpriseRequests;

    @JsonProperty("createdAt")
    private LocalDate createdAt;

    @JsonProperty("updatedAt")
    private LocalDate updatedAt;

    @JsonProperty("daysActive")
    private Long daysActive;

    @JsonProperty("status")
    private String status;

    @JsonProperty("visibility")
    private String visibility;

    // Constructors
    public AnalyticsDTO() {}

    public AnalyticsDTO(Integer projectId, String projectTitle, Integer viewCount,
            Integer enterpriseRequests, LocalDate createdAt, LocalDate updatedAt,
            Long daysActive, String status, String visibility) {
        this.projectId = projectId;
        this.projectTitle = projectTitle;
        this.viewCount = viewCount;
        this.enterpriseRequests = enterpriseRequests;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.daysActive = daysActive;
        this.status = status;
        this.visibility = visibility;
    }

    // Getters and Setters
    public Integer getProjectId() { return projectId; }
    public void setProjectId(Integer projectId) { this.projectId = projectId; }

    public String getProjectTitle() { return projectTitle; }
    public void setProjectTitle(String projectTitle) { this.projectTitle = projectTitle; }

    public Integer getViewCount() { return viewCount; }
    public void setViewCount(Integer viewCount) { this.viewCount = viewCount; }

    public Integer getEnterpriseRequests() { return enterpriseRequests; }
    public void setEnterpriseRequests(Integer enterpriseRequests) { this.enterpriseRequests = enterpriseRequests; }

    public LocalDate getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDate createdAt) { this.createdAt = createdAt; }

    public LocalDate getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDate updatedAt) { this.updatedAt = updatedAt; }

    public Long getDaysActive() { return daysActive; }
    public void setDaysActive(Long daysActive) { this.daysActive = daysActive; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getVisibility() { return visibility; }
    public void setVisibility(String visibility) { this.visibility = visibility; }
}
