package com.learn.brainbridge.service;

import com.learn.brainbridge.dtos.AnalyticsDTO;
import com.learn.brainbridge.entity.Projects;
import com.learn.brainbridge.repository.AnalyticsRepository;
import com.learn.brainbridge.repository.ProjectsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class AnalyticsService {

    private final ProjectsRepository projectRepository;
    private final AnalyticsRepository analyticsRepository;

    @Autowired
    public AnalyticsService(ProjectsRepository projectRepository,
                            AnalyticsRepository analyticsRepository) {
        this.projectRepository = projectRepository;
        this.analyticsRepository = analyticsRepository;
    }

    
    public AnalyticsDTO getProjectAnalytics(Integer projectId) {

        Projects project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        AnalyticsDTO dto = new AnalyticsDTO();

        
        dto.setProjectId(project.getId());
        dto.setProjectTitle(project.getTitle());

        dto.setStatus(project.getProjectStatus().name());
        dto.setVisibility(project.getProjectVisibility().name());

        dto.setCreatedAt(project.getCreatedAt());
        dto.setUpdatedAt(project.getUpdatedAt());

        
        dto.setViewCount(
                analyticsRepository.getViewCount(projectId)
        );

        dto.setEnterpriseRequests(
                analyticsRepository.getEnterpriseRequests(projectId)
        );

        
        long daysActive = ChronoUnit.DAYS.between(
                project.getCreatedAt(),
                LocalDate.now()
        );

        dto.setDaysActive(daysActive);

        return dto;
    }
}