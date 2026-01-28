package org.henriette.brainbridge_v2.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.henriette.brainbridge_v2.Enums.Status;
import org.henriette.brainbridge_v2.Enums.Visibility;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDate;

public class ProjectDTO {


    private Integer id;
    @NotBlank
    @Max(50)
    @Min(10)
    @Schema(description = "Project title",example = "BrainBridge")
    private String title;
    @NotBlank
    @Max(500)
    @Min(100)
    @Schema(description = "Project description",example = "This project helps to show case your projects and " +
            "also connect with people who are interested in your project")
    private String description;
    @Schema(description = "Project status",example = "Finished")
    private Status projectStatus;
    @Schema(description = "Project Visibility",example = "Public")
    private Visibility projectVisibility;
    @Schema(description = "Owner id",example = "101")
    private Integer ownerId;
    @Schema(description = "Team id",example = "100")
    private Integer teamId;
    @Schema(description = "Source idea id")
    private Integer sourceIdeaId;
    @Schema(description = "Cover image url",example = "image.png")
    private String cover_image_url;
    @NotBlank
    @URL
    @Schema(description = "Project repository url",example = "https://github.com/BrainBridge")
    private String repoUrl;

    private LocalDate startDate;
    private LocalDate endDate;

    public ProjectDTO() {
    }

    public ProjectDTO(Integer id, String title, String description,
                       Status project_status, Visibility project_visibility,
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
        this.cover_image_url = cover_image_url;
        this.repoUrl = repo_url;
        this.startDate = start_date;
        this.endDate = end_date;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Status getProject_status() { return projectStatus; }
    public void setProject_status(Status project_status) { this.projectStatus = project_status; }

    public Visibility getProject_visibility() { return projectVisibility; }
    public void setProject_visibility(Visibility project_visibility) { this.projectVisibility = project_visibility; }

    public Integer getOwner_id() { return ownerId; }
    public void setOwner_id(Integer owner_id) { this.ownerId = owner_id; }

    public Integer getTeam_id() { return teamId; }
    public void setTeam_id(Integer team_id) { this.teamId = team_id; }

    public Integer getSource_idea_id() { return sourceIdeaId; }
    public void setSource_idea_id(Integer source_idea_id) { this.sourceIdeaId = source_idea_id; }

    public String getCover_image_url() { return cover_image_url; }
    public void setCover_image_url(String cover_image_url) { this.cover_image_url = cover_image_url; }

    public String getRepo_url() { return repoUrl; }
    public void setRepo_url(String repo_url) { this.repoUrl = repo_url; }

    public LocalDate getStart_date() { return startDate; }
    public void setStart_date(LocalDate start_date) { this.startDate = start_date; }

    public LocalDate getEnd_date() { return endDate; }
    public void setEnd_date(LocalDate end_date) { this.endDate = end_date; }
}
