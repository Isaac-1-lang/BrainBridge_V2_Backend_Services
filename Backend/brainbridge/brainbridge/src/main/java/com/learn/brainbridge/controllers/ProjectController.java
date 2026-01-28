package com.learn.brainbridge.controllers;

import com.learn.brainbridge.dtos.ProjectDTO;
import com.learn.brainbridge.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ProjectController - REST API Controller for Project operations
 * 
 * CONCEPTS TO LEARN:
 * 1. @Tag - Groups endpoints in Swagger UI
 * 2. @Operation - Describes what the endpoint does
 * 3. @ApiResponses - Documents possible HTTP responses
 * 4. @Parameter - Documents path/query parameters
 */
@RestController
@RequestMapping("/api/projects")
@Tag(name = "Projects", description = "API endpoints for managing coding projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    /**
     * POST /api/projects
     * Create a new project
     */
    @PostMapping
    @Operation(
            summary = "Create a new project",
            description = "Creates a new coding project. The project will be associated with the authenticated user."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Project created successfully",
                    content = @Content(schema = @Schema(implementation = ProjectDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<ProjectDTO> createProject(
            @Valid @RequestBody ProjectDTO projectDTO,
            @Parameter(description = "User ID of the project creator", required = true)
            @RequestParam Long userId) {
        ProjectDTO createdProject = projectService.createProject(projectDTO, userId);
        return new ResponseEntity<>(createdProject, HttpStatus.CREATED);
    }

    /**
     * GET /api/projects/{id}
     * Get project by ID
     */
    @GetMapping("/{id}")
    @Operation(
            summary = "Get project by ID",
            description = "Retrieves a project by its ID. Automatically increments the view count."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Project found",
                    content = @Content(schema = @Schema(implementation = ProjectDTO.class))),
            @ApiResponse(responseCode = "404", description = "Project not found")
    })
    public ResponseEntity<ProjectDTO> getProjectById(
            @Parameter(description = "Project ID", required = true)
            @PathVariable Long id) {
        ProjectDTO project = projectService.getProjectById(id);
        return ResponseEntity.ok(project);
    }

    /**
     * GET /api/projects
     * Get all public projects
     */
    @GetMapping
    @Operation(
            summary = "Get all public projects",
            description = "Retrieves a list of all public and active projects."
    )
    @ApiResponse(responseCode = "200", description = "List of projects retrieved successfully")
    public ResponseEntity<List<ProjectDTO>> getAllProjects() {
        List<ProjectDTO> projects = projectService.getAllProjects();
        return ResponseEntity.ok(projects);
    }

    /**
     * GET /api/projects/user/{userId}
     * Get all projects by user ID
     */
    @GetMapping("/user/{userId}")
    @Operation(
            summary = "Get projects by user ID",
            description = "Retrieves all active projects created by a specific user."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Projects retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<List<ProjectDTO>> getProjectsByUserId(
            @Parameter(description = "User ID", required = true)
            @PathVariable Long userId) {
        List<ProjectDTO> projects = projectService.getProjectsByUserId(userId);
        return ResponseEntity.ok(projects);
    }

    /**
     * GET /api/projects/public
     * Get all public projects
     */
    @GetMapping("/public")
    @Operation(
            summary = "Get all public projects",
            description = "Retrieves all public and active projects."
    )
    @ApiResponse(responseCode = "200", description = "Public projects retrieved successfully")
    public ResponseEntity<List<ProjectDTO>> getPublicProjects() {
        List<ProjectDTO> projects = projectService.getPublicProjects();
        return ResponseEntity.ok(projects);
    }

    /**
     * PUT /api/projects/{id}
     * Update project
     */
    @PutMapping("/{id}")
    @Operation(
            summary = "Update project",
            description = "Updates an existing project. Only the project owner can update it."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Project updated successfully",
                    content = @Content(schema = @Schema(implementation = ProjectDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input or unauthorized"),
            @ApiResponse(responseCode = "404", description = "Project not found")
    })
    public ResponseEntity<ProjectDTO> updateProject(
            @Parameter(description = "Project ID", required = true)
            @PathVariable Long id,
            @Valid @RequestBody ProjectDTO projectDTO,
            @Parameter(description = "User ID for authorization", required = true)
            @RequestParam Long userId) {
        ProjectDTO updatedProject = projectService.updateProject(id, projectDTO, userId);
        return ResponseEntity.ok(updatedProject);
    }

    /**
     * DELETE /api/projects/{id}
     * Delete project
     */
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete project",
            description = "Soft deletes a project (sets isActive to false). Only the project owner can delete it."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Project deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Project not found")
    })
    public ResponseEntity<Void> deleteProject(
            @Parameter(description = "Project ID", required = true)
            @PathVariable Long id,
            @Parameter(description = "User ID for authorization", required = true)
            @RequestParam Long userId) {
        projectService.deleteProject(id, userId);
        return ResponseEntity.noContent().build();
    }

    /**
     * POST /api/projects/{id}/view
     * Increment view count
     */
    @PostMapping("/{id}/view")
    @Operation(
            summary = "Increment view count",
            description = "Increments the view count for a project."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "View count incremented"),
            @ApiResponse(responseCode = "404", description = "Project not found")
    })
    public ResponseEntity<Void> incrementViewCount(
            @Parameter(description = "Project ID", required = true)
            @PathVariable Long id) {
        projectService.incrementViewCount(id);
        return ResponseEntity.ok().build();
    }

    /**
     * POST /api/projects/{id}/like
     * Increment like count
     */
    @PostMapping("/{id}/like")
    @Operation(
            summary = "Increment like count",
            description = "Increments the like count for a project."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Like count incremented"),
            @ApiResponse(responseCode = "404", description = "Project not found")
    })
    public ResponseEntity<Void> incrementLikeCount(
            @Parameter(description = "Project ID", required = true)
            @PathVariable Long id) {
        projectService.incrementLikeCount(id);
        return ResponseEntity.ok().build();
    }

    /**
     * DELETE /api/projects/{id}/like
     * Decrement like count
     */
    @DeleteMapping("/{id}/like")
    @Operation(
            summary = "Decrement like count",
            description = "Decrements the like count for a project."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Like count decremented"),
            @ApiResponse(responseCode = "404", description = "Project not found")
    })
    public ResponseEntity<Void> decrementLikeCount(
            @Parameter(description = "Project ID", required = true)
            @PathVariable Long id) {
        projectService.decrementLikeCount(id);
        return ResponseEntity.ok().build();
    }
}
