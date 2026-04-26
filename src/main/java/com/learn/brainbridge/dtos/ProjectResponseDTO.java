package com.learn.brainbridge.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.learn.brainbridge.enums.ProjectStatus;
import com.learn.brainbridge.enums.ProjectVisibility;
import java.time.LocalDate;
import java.util.List;

public class ProjectResponseDTO {
    @JsonProperty("id")
    private Integer id;

    @JsonProperty("title")
    private String title;

    @JsonProperty("description")
    private String description;

    @JsonProperty("projectStatus")
    private ProjectStatus projectStatus;

    @JsonProperty("projectVisibility")
    private ProjectVisibility projectVisibility;

    @JsonProperty("ownerId")
    private Integer ownerId;

    @JsonProperty("teamId")
    private Integer teamId;

    @JsonProperty("sourceIdeaId")
    private Integer sourceIdeaId;

    @JsonProperty("coverImageUrl")
    private String coverImageUrl;

    @JsonProperty("repoUrl")
    private String repoUrl;

    @JsonProperty("startDate")
    private LocalDate startDate;

    @JsonProperty("endDate")
    private LocalDate endDate;

    @JsonProperty("viewCount")
    private Integer viewCount;

    @JsonProperty("enterpriseRequests")
    private Integer enterpriseRequests;

    @JsonProperty("field")
    private String field;

    @JsonProperty("mainTags")
    private List<String> mainTags;

    @JsonProperty("subTags")
    private List<String> subTags;

    @JsonProperty("sdgGoals")
    private List<String> sdgGoals;

    @JsonProperty("nst2Goals")
    private List<String> nst2Goals;

    @JsonProperty("additionalMediaUrls")
    private List<String> additionalMediaUrls;

    @JsonProperty("createdAt")
    private LocalDate createdAt;

    @JsonProperty("updatedAt")
    private LocalDate updatedAt;

    // Constructors
    public ProjectResponseDTO() {}

    public ProjectResponseDTO(Integer id, String title, String description, ProjectStatus projectStatus,
            ProjectVisibility projectVisibility, Integer ownerId, Integer teamId, Integer sourceIdeaId,
            String coverImageUrl, String repoUrl, LocalDate startDate, LocalDate endDate,
            Integer viewCount, Integer enterpriseRequests, String field, List<String> mainTags,
            List<String> subTags, List<String> sdgGoals, List<String> nst2Goals,
            List<String> additionalMediaUrls, LocalDate createdAt, LocalDate updatedAt) {
        this.id = id;
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
        this.viewCount = viewCount;
        this.enterpriseRequests = enterpriseRequests;
        this.field = field;
        this.mainTags = mainTags;
        this.subTags = subTags;
        this.sdgGoals = sdgGoals;
        this.nst2Goals = nst2Goals;
        this.additionalMediaUrls = additionalMediaUrls;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public ProjectStatus getProjectStatus() { return projectStatus; }
    public void setProjectStatus(ProjectStatus projectStatus) { this.projectStatus = projectStatus; }

    public ProjectVisibility getProjectVisibility() { return projectVisibility; }
    public void setProjectVisibility(ProjectVisibility projectVisibility) { this.projectVisibility = projectVisibility; }

    public Integer getOwnerId() { return ownerId; }
    public void setOwnerId(Integer ownerId) { this.ownerId = ownerId; }

    public Integer getTeamId() { return teamId; }
    public void setTeamId(Integer teamId) { this.teamId = teamId; }

    public Integer getSourceIdeaId() { return sourceIdeaId; }
    public void setSourceIdeaId(Integer sourceIdeaId) { this.sourceIdeaId = sourceIdeaId; }

    public String getCoverImageUrl() { return coverImageUrl; }
    public void setCoverImageUrl(String coverImageUrl) { this.coverImageUrl = coverImageUrl; }

    public String getRepoUrl() { return repoUrl; }
    public void setRepoUrl(String repoUrl) { this.repoUrl = repoUrl; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public Integer getViewCount() { return viewCount; }
    public void setViewCount(Integer viewCount) { this.viewCount = viewCount; }

    public Integer getEnterpriseRequests() { return enterpriseRequests; }
    public void setEnterpriseRequests(Integer enterpriseRequests) { this.enterpriseRequests = enterpriseRequests; }

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

    public LocalDate getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDate createdAt) { this.createdAt = createdAt; }

    public LocalDate getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDate updatedAt) { this.updatedAt = updatedAt; }
}
