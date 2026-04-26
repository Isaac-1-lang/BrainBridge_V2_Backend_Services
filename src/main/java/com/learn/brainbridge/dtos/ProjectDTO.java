package com.learn.brainbridge.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import com.learn.brainbridge.enums.ProjectStatus;
import com.learn.brainbridge.enums.ProjectVisibility;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProjectDTO {

    @JsonProperty("id")
    private Integer id;

    @NotBlank
    @Size(min = 2, max = 50)
    @Schema(description = "Project title", example = "BrainBridge Project")
    @JsonProperty("title")
    private String title;

    @NotBlank
    @Size(min = 2, max = 5000)
    @Schema(description = "Project description", example = "This project helps to show case your projects and " +
            "also connect with people who are interested in your project")
    @JsonProperty("description")
    private String description;

    @NotNull
    @Schema(description = "Project status", example = "ACTIVE")
    @JsonProperty("projectStatus")
    private ProjectStatus projectStatus;

    @NotNull
    @Schema(description = "Project Visibility", example = "PUBLIC")
    @JsonProperty("projectVisibility")
    private ProjectVisibility projectVisibility;

    @Schema(description = "Owner id", example = "101")
    @JsonProperty("ownerId")
    private Integer ownerId;

    @Positive
    @Schema(description = "Team id", example = "100")
    @JsonProperty("teamId")
    private Integer teamId;

    @Positive
    @Schema(description = "Source idea id", example = "999")
    @JsonProperty("sourceIdeaId")
    private Integer sourceIdeaId;

    @Schema(description = "Cover image url", example = "image.png")
    @JsonProperty("coverImageUrl")
    private String coverImageUrl;

    @Schema(description = "Project repository url", example = "https://github.com/BrainBridge")
    @JsonProperty("repoUrl")
    private String repoUrl;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonProperty("startDate")
    private LocalDate startDate;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonProperty("endDate")
    private LocalDate endDate;
    @JsonProperty("viewCount")
    private Integer viewCount = 0;

    @JsonProperty("enterpriseRequests")
    private Integer enterpriseRequests = 0;

    @JsonProperty("field")
    private String field;

    @JsonProperty("mainTags")
    private List<String> mainTags = new ArrayList<>();

    @JsonProperty("subTags")
    private List<String> subTags = new ArrayList<>();

    @JsonProperty("sdgGoals")
    private List<String> sdgGoals = new ArrayList<>();

    @JsonProperty("nst2Goals")
    private List<String> nst2Goals = new ArrayList<>();

    @JsonProperty("additionalMediaUrls")
    private List<String> additionalMediaUrls = new ArrayList<>();

    public ProjectDTO() {
    }

    public ProjectDTO(Integer id, String title, String description,
            ProjectStatus project_status, ProjectVisibility project_visibility,
            Integer owner_id, Integer team_id, Integer source_idea_id,
            String cover_image_url, String repo_url,
            LocalDate start_date, LocalDate end_date) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.projectStatus = project_status;
        this.projectVisibility = project_visibility;
        this.ownerId = owner_id;
        this.teamId = team_id;
        this.sourceIdeaId = source_idea_id;
        this.coverImageUrl = cover_image_url;
        this.repoUrl = repo_url;
        this.startDate = start_date;
        this.endDate = end_date;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ProjectStatus getProjectStatus() {
        return projectStatus;
    }

    public void setProjectStatus(ProjectStatus projectStatus) {
        this.projectStatus = projectStatus;
    }

    public ProjectVisibility getProjectVisibility() {
        return projectVisibility;
    }

    public void setProjectVisibility(ProjectVisibility projectVisibility) {
        this.projectVisibility = projectVisibility;
    }

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }

    public Integer getTeamId() {
        return teamId;
    }

    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
    }

    public Integer getSourceIdeaId() {
        return sourceIdeaId;
    }

    public void setSourceIdeaId(Integer sourceIdeaId) {
        this.sourceIdeaId = sourceIdeaId;
    }

    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    public void setCoverImageUrl(String coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
    }

    public String getRepoUrl() {
        return repoUrl;
    }

    public void setRepoUrl(String repoUrl) {
        this.repoUrl = repoUrl;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
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

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public List<String> getMainTags() {
        return mainTags;
    }

    public void setMainTags(List<String> mainTags) {
        this.mainTags = mainTags;
    }

    public List<String> getSubTags() {
        return subTags;
    }

    public void setSubTags(List<String> subTags) {
        this.subTags = subTags;
    }

    public List<String> getSdgGoals() {
        return sdgGoals;
    }

    public void setSdgGoals(List<String> sdgGoals) {
        this.sdgGoals = sdgGoals;
    }

    public List<String> getNst2Goals() {
        return nst2Goals;
    }

    public void setNst2Goals(List<String> nst2Goals) {
        this.nst2Goals = nst2Goals;
    }

    public List<String> getAdditionalMediaUrls() {
        return additionalMediaUrls;
    }

    public void setAdditionalMediaUrls(List<String> additionalMediaUrls) {
        this.additionalMediaUrls = additionalMediaUrls;
    }
}