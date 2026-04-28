package com.learn.brainbridge.service;

import com.learn.brainbridge.dtos.AnalyticsDTO;
import com.learn.brainbridge.entity.Projects;
import com.learn.brainbridge.repository.ProjectsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProjectsService {

    private final ProjectsRepository repo;

    @Autowired
    public ProjectsService(ProjectsRepository repo) {
        this.repo = repo;
    }

    public Projects createProject(Projects project) {
        return repo.save(project);
    }

    public List<Projects> getAllProjects() {
        return repo.findAll();
    }

    public Optional<Projects> getProjectById(Integer id) {
        return repo.findById(id);
    }

    public Optional<Projects> getProjectByTitle(String title) {
        return repo.findProjectsByTitle(title);
    }

    public Projects updateProject(Projects project) {
        return repo.save(project);
    }

    public void deleteProject(Integer id) {
        repo.deleteById(id);
    }

    public List<Projects> getProjectsByOwner(Integer ownerId) {
        return repo.findByOwnerId(ownerId);
    }

    public List<Projects> getProjectsByTeam(Integer teamId) {
        return repo.findByTeamId(teamId);
    }

    public void incrementViewCount(Integer id) {
        repo.findById(id).ifPresent(project -> {
            project.setViewCount(project.getViewCount() + 1);
            repo.save(project);
        });
    }

    // Analytics methods
    public AnalyticsDTO getProjectAnalytics(Integer projectId) {
        Optional<Projects> projectOpt = repo.findById(projectId);
        if (projectOpt.isEmpty()) {
            return null;
        }

        Projects project = projectOpt.get();
        long daysActive = ChronoUnit.DAYS.between(project.getCreatedAt(), LocalDate.now());

        return new AnalyticsDTO(
            project.getId(),
            project.getTitle(),
            project.getViewCount(),
            project.getEnterpriseRequests(),
            project.getCreatedAt(),
            project.getUpdatedAt(),
            daysActive,
            project.getProjectStatus().toString(),
            project.getProjectVisibility().toString()
        );
    }

    public List<AnalyticsDTO> getOwnerProjectsAnalytics(Integer ownerId) {
        List<Projects> projects = repo.findByOwnerId(ownerId);
        return projects.stream()
            .map(project -> {
                long daysActive = ChronoUnit.DAYS.between(project.getCreatedAt(), LocalDate.now());
                return new AnalyticsDTO(
                    project.getId(),
                    project.getTitle(),
                    project.getViewCount(),
                    project.getEnterpriseRequests(),
                    project.getCreatedAt(),
                    project.getUpdatedAt(),
                    daysActive,
                    project.getProjectStatus().toString(),
                    project.getProjectVisibility().toString()
                );
            })
            .collect(Collectors.toList());
    }

    public List<AnalyticsDTO> getAllProjectsAnalytics() {
        List<Projects> projects = repo.findAll();
        return projects.stream()
            .map(project -> {
                long daysActive = ChronoUnit.DAYS.between(project.getCreatedAt(), LocalDate.now());
                return new AnalyticsDTO(
                    project.getId(),
                    project.getTitle(),
                    project.getViewCount(),
                    project.getEnterpriseRequests(),
                    project.getCreatedAt(),
                    project.getUpdatedAt(),
                    daysActive,
                    project.getProjectStatus().toString(),
                    project.getProjectVisibility().toString()
                );
            })
            .collect(Collectors.toList());
    }

        // =========================
    // ANALYTICS METHODS
    // =========================

    public Integer getProjectViews(Integer id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"))
                .getViewCount();
    }

    public Integer getEnterpriseRequests(Integer id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"))
                .getEnterpriseRequests();
    }

    public String getProjectStatus(Integer id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"))
                .getProjectStatus()
                .name();
    }

    public String getProjectVisibility(Integer id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"))
                .getProjectVisibility()
                .name();
    }

    // NOTE: No real members system exists in your entity yet
    // This is a safe placeholder for future implementation
    public Integer getProjectMembersCount(Integer id) {
        Projects project = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        if (project.getTeamId() == null) {
            return 0;
        }

        // TODO: Replace this when TeamMember entity is added
        return 0;
    }
}