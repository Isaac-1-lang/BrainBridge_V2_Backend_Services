package com.learn.brainbridge.service;

import com.learn.brainbridge.dtos.ProjectDTO;

import java.util.List;

/**
 * ProjectService Interface - Defines business logic operations for Projects
 */
public interface ProjectService {
    
    /**
     * Create a new project
     * @param projectDTO Project data
     * @param userId User ID who is creating the project
     * @return Created project DTO
     */
    ProjectDTO createProject(ProjectDTO projectDTO, Long userId);
    
    /**
     * Get project by ID
     * @param id Project ID
     * @return Project DTO
     */
    ProjectDTO getProjectById(Long id);
    
    /**
     * Get all projects (public only if not owner)
     * @return List of project DTOs
     */
    List<ProjectDTO> getAllProjects();
    
    /**
     * Get all projects by user ID
     * @param userId User ID
     * @return List of project DTOs
     */
    List<ProjectDTO> getProjectsByUserId(Long userId);
    
    /**
     * Get public projects
     * @return List of public project DTOs
     */
    List<ProjectDTO> getPublicProjects();
    
    /**
     * Update project
     * @param id Project ID
     * @param projectDTO Updated project data
     * @param userId User ID (for authorization)
     * @return Updated project DTO
     */
    ProjectDTO updateProject(Long id, ProjectDTO projectDTO, Long userId);
    
    /**
     * Delete project
     * @param id Project ID
     * @param userId User ID (for authorization)
     */
    void deleteProject(Long id, Long userId);
    
    /**
     * Increment view count for a project
     * @param id Project ID
     */
    void incrementViewCount(Long id);
    
    /**
     * Increment like count for a project
     * @param id Project ID
     */
    void incrementLikeCount(Long id);
    
    /**
     * Decrement like count for a project
     * @param id Project ID
     */
    void decrementLikeCount(Long id);
}

