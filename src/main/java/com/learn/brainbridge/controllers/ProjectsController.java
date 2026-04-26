package com.learn.brainbridge.controllers;

import com.learn.brainbridge.dtos.ProjectDTO;
import com.learn.brainbridge.dtos.ProjectResponseDTO;
import com.learn.brainbridge.entity.Projects;
import com.learn.brainbridge.entity.User;
import com.learn.brainbridge.repository.UserRepository;
import com.learn.brainbridge.service.ProjectsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

@RestController
@Tag(name = "Projects API", description = "Projects operations(ADD,GET,DELETE OR UPDATE)")
@RequestMapping("/projects")
public class ProjectsController {
    private static final Logger log = LoggerFactory.getLogger(ProjectsController.class);
    private final ProjectsService service;
    private final UserRepository userRepository;

    @Autowired
    public ProjectsController(ProjectsService service, UserRepository userRepository) {
        this.service = service;
        this.userRepository = userRepository;
    }

    @PostMapping("/add")
    public ResponseEntity<?> createProject(Authentication authentication, @Valid @RequestBody ProjectDTO projectDTO) {
        log.info("=== Project Creation Request ===");
        log.info("  Title: {}", projectDTO.getTitle());
        log.info("  Description: {}", projectDTO.getDescription());
        log.info("  Field: {}", projectDTO.getField());
        log.info("  Authentication: {}", authentication);
        log.info("  Principal: {}", authentication != null ? authentication.getName() : "NULL");

        if (authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getName())) {
            log.warn("  REJECTED: No valid authentication");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(java.util.Map.of("error", "Unauthorized"));
        }

        String principalName = authentication.getName();
        User user = userRepository.findByEmail(principalName)
                .orElseGet(() -> userRepository.findByUsername(principalName).orElse(null));

        if (user == null) {
            log.warn("  REJECTED: User not found for principal: {}", principalName);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        log.info("  User found: {} (ID: {})", user.getUsername(), user.getId());

        Integer ownerId;
        try {
            ownerId = Math.toIntExact(user.getId());
        } catch (ArithmeticException ex) {
            log.error("  User ID too large: {}", user.getId());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("User ID is too large to map to project owner id");
        }

        Projects project = new Projects();
        project.setTitle(projectDTO.getTitle());
        project.setDescription(projectDTO.getDescription());
        project.setOwnerId(ownerId);
        project.setTeamId(projectDTO.getTeamId());
        project.setSourceIdeaId(projectDTO.getSourceIdeaId());
        project.setCoverImageUrl(projectDTO.getCoverImageUrl());
        project.setRepoUrl(projectDTO.getRepoUrl());
        project.setStartDate(projectDTO.getStartDate());
        project.setEndDate(projectDTO.getEndDate());
        project.setProjectStatus(projectDTO.getProjectStatus());
        project.setProjectVisibility(projectDTO.getProjectVisibility());
        project.setField(projectDTO.getField());
        project.setMainTags(projectDTO.getMainTags() == null ? new ArrayList<>() : projectDTO.getMainTags());
        project.setSubTags(projectDTO.getSubTags() == null ? new ArrayList<>() : projectDTO.getSubTags());
        project.setSdgGoals(projectDTO.getSdgGoals() == null ? new ArrayList<>() : projectDTO.getSdgGoals());
        project.setNst2Goals(projectDTO.getNst2Goals() == null ? new ArrayList<>() : projectDTO.getNst2Goals());
        project.setAdditionalMediaUrls(projectDTO.getAdditionalMediaUrls() == null ? new ArrayList<>() : projectDTO.getAdditionalMediaUrls());
        project.setCreatedAt(java.time.LocalDate.now());
        project.setUpdatedAt(java.time.LocalDate.now());
        
        log.info("  Creating project with title: {}", project.getTitle());
        Projects saved = service.createProject(project);
        log.info("  Project created successfully with ID: {}", saved.getId());
        
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllProjects() {
        List<Projects> projects = service.getAllProjects();
        if (projects.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("No projects found");
        }
        
        // Convert to DTOs to avoid Hibernate serialization issues
        List<ProjectResponseDTO> projectDTOs = projects.stream()
            .map(p -> new ProjectResponseDTO(
                p.getId(),
                p.getTitle(),
                p.getDescription(),
                p.getProjectStatus(),
                p.getProjectVisibility(),
                p.getOwnerId(),
                p.getTeamId(),
                p.getSourceIdeaId(),
                p.getCoverImageUrl(),
                p.getRepoUrl(),
                p.getStartDate(),
                p.getEndDate(),
                p.getViewCount(),
                p.getEnterpriseRequests(),
                p.getField(),
                p.getMainTags() != null ? new java.util.ArrayList<>(p.getMainTags()) : new java.util.ArrayList<>(),
                p.getSubTags() != null ? new java.util.ArrayList<>(p.getSubTags()) : new java.util.ArrayList<>(),
                p.getSdgGoals() != null ? new java.util.ArrayList<>(p.getSdgGoals()) : new java.util.ArrayList<>(),
                p.getNst2Goals() != null ? new java.util.ArrayList<>(p.getNst2Goals()) : new java.util.ArrayList<>(),
                p.getAdditionalMediaUrls() != null ? new java.util.ArrayList<>(p.getAdditionalMediaUrls()) : new java.util.ArrayList<>(),
                p.getCreatedAt(),
                p.getUpdatedAt()
            ))
            .collect(java.util.stream.Collectors.toList());
        
        return ResponseEntity.status(HttpStatus.OK).body(projectDTOs);
    }

    @GetMapping("/my")
        @Transactional(readOnly = true)
        public ResponseEntity<?> getMyProjects(Authentication authentication) {
            log.info("=== Get My Projects Request ===");
            log.info("  Authentication: {}", authentication);
            log.info("  Principal: {}", authentication != null ? authentication.getName() : "NULL");

            if (authentication == null || !authentication.isAuthenticated()
                    || "anonymousUser".equals(authentication.getName())) {
                log.warn("  REJECTED: No valid authentication");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(java.util.Map.of("error", "Unauthorized"));
            }

            String principalName = authentication.getName();
            log.info("  Looking up user by email/username: {}", principalName);

            // Try to resolve the current user by email first, then by username
            User user = userRepository.findByEmail(principalName)
                    .orElseGet(() -> userRepository.findByUsername(principalName).orElse(null));

            if (user == null) {
                log.warn("  REJECTED: User not found for principal: {}", principalName);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }

            log.info("  User found: {} (ID: {})", user.getUsername(), user.getId());

            Integer ownerId;
            try {
                ownerId = Math.toIntExact(user.getId());
            } catch (ArithmeticException ex) {
                log.error("  User ID too large: {}", user.getId());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("User ID is too large to map to project owner id");
            }

            try {
                log.info("  Fetching projects for owner ID: {}", ownerId);
                List<Projects> projects = service.getProjectsByOwner(ownerId);
                log.info("  Found {} projects", projects.size());
                
                // Convert to DTOs to avoid Hibernate serialization issues
                List<ProjectResponseDTO> projectDTOs = projects.stream()
                    .map(p -> new ProjectResponseDTO(
                        p.getId(),
                        p.getTitle(),
                        p.getDescription(),
                        p.getProjectStatus(),
                        p.getProjectVisibility(),
                        p.getOwnerId(),
                        p.getTeamId(),
                        p.getSourceIdeaId(),
                        p.getCoverImageUrl(),
                        p.getRepoUrl(),
                        p.getStartDate(),
                        p.getEndDate(),
                        p.getViewCount(),
                        p.getEnterpriseRequests(),
                        p.getField(),
                        p.getMainTags() != null ? new java.util.ArrayList<>(p.getMainTags()) : new java.util.ArrayList<>(),
                        p.getSubTags() != null ? new java.util.ArrayList<>(p.getSubTags()) : new java.util.ArrayList<>(),
                        p.getSdgGoals() != null ? new java.util.ArrayList<>(p.getSdgGoals()) : new java.util.ArrayList<>(),
                        p.getNst2Goals() != null ? new java.util.ArrayList<>(p.getNst2Goals()) : new java.util.ArrayList<>(),
                        p.getAdditionalMediaUrls() != null ? new java.util.ArrayList<>(p.getAdditionalMediaUrls()) : new java.util.ArrayList<>(),
                        p.getCreatedAt(),
                        p.getUpdatedAt()
                    ))
                    .collect(java.util.stream.Collectors.toList());
                
                log.info("  Converted to {} DTOs", projectDTOs.size());
                return ResponseEntity.status(HttpStatus.OK).body(projectDTOs);
            } catch (Exception ex) {
                log.error("  ERROR fetching projects: {}", ex.getMessage(), ex);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(java.util.Map.of("error", "Failed to load your projects: " + ex.getMessage()));
            }
        }


    @PutMapping("/update")
    public ResponseEntity<?> updateProject(@Valid @RequestBody ProjectDTO projectDTO) {
        if (projectDTO.getId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Project id is required for update");
        }

        Optional<Projects> existingOpt = service.getProjectById(projectDTO.getId());
        if (existingOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Project not found");
        }

        Projects project = existingOpt.get();
        project.setTitle(projectDTO.getTitle());
        project.setDescription(projectDTO.getDescription());
        project.setOwnerId(projectDTO.getOwnerId());
        project.setTeamId(projectDTO.getTeamId());
        project.setSourceIdeaId(projectDTO.getSourceIdeaId());
        project.setCoverImageUrl(projectDTO.getCoverImageUrl());
        project.setRepoUrl(projectDTO.getRepoUrl());
        project.setStartDate(projectDTO.getStartDate());
        project.setEndDate(projectDTO.getEndDate());
        project.setProjectStatus(projectDTO.getProjectStatus());
        project.setProjectVisibility(projectDTO.getProjectVisibility());
        project.setField(projectDTO.getField());
        project.setMainTags(projectDTO.getMainTags() == null ? new ArrayList<>() : projectDTO.getMainTags());
        project.setSubTags(projectDTO.getSubTags() == null ? new ArrayList<>() : projectDTO.getSubTags());
        project.setSdgGoals(projectDTO.getSdgGoals() == null ? new ArrayList<>() : projectDTO.getSdgGoals());
        project.setNst2Goals(projectDTO.getNst2Goals() == null ? new ArrayList<>() : projectDTO.getNst2Goals());
        project.setAdditionalMediaUrls(projectDTO.getAdditionalMediaUrls() == null ? new ArrayList<>() : projectDTO.getAdditionalMediaUrls());
        project.setUpdatedAt(java.time.LocalDate.now());

        Projects updated = service.updateProject(project);
        return ResponseEntity.status(HttpStatus.OK).body(updated);
    }

    @GetMapping("/team/{teamName}")
    public ResponseEntity<?> getProject(@PathVariable("teamName") Integer teamName) {
        List<Projects> projects = service.getProjectsByTeam(teamName);
        if (projects.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No projects found for team " + teamName);
        }
        
        // Convert to DTOs to avoid Hibernate serialization issues
        List<ProjectResponseDTO> projectDTOs = projects.stream()
            .map(p -> new ProjectResponseDTO(
                p.getId(),
                p.getTitle(),
                p.getDescription(),
                p.getProjectStatus(),
                p.getProjectVisibility(),
                p.getOwnerId(),
                p.getTeamId(),
                p.getSourceIdeaId(),
                p.getCoverImageUrl(),
                p.getRepoUrl(),
                p.getStartDate(),
                p.getEndDate(),
                p.getViewCount(),
                p.getEnterpriseRequests(),
                p.getField(),
                p.getMainTags() != null ? new java.util.ArrayList<>(p.getMainTags()) : new java.util.ArrayList<>(),
                p.getSubTags() != null ? new java.util.ArrayList<>(p.getSubTags()) : new java.util.ArrayList<>(),
                p.getSdgGoals() != null ? new java.util.ArrayList<>(p.getSdgGoals()) : new java.util.ArrayList<>(),
                p.getNst2Goals() != null ? new java.util.ArrayList<>(p.getNst2Goals()) : new java.util.ArrayList<>(),
                p.getAdditionalMediaUrls() != null ? new java.util.ArrayList<>(p.getAdditionalMediaUrls()) : new java.util.ArrayList<>(),
                p.getCreatedAt(),
                p.getUpdatedAt()
            ))
            .collect(java.util.stream.Collectors.toList());
        
        return ResponseEntity.status(HttpStatus.OK).body(projectDTOs);
    }

    @GetMapping("/fetch/{id}")
    public ResponseEntity<?> getProjectById(@PathVariable("id") Integer id) {
        Optional<Projects> project = service.getProjectById(id);
        if (project.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No projects found");
        }
        
        // Convert to DTO to avoid Hibernate serialization issues
        Projects p = project.get();
        ProjectResponseDTO projectDTO = new ProjectResponseDTO(
            p.getId(),
            p.getTitle(),
            p.getDescription(),
            p.getProjectStatus(),
            p.getProjectVisibility(),
            p.getOwnerId(),
            p.getTeamId(),
            p.getSourceIdeaId(),
            p.getCoverImageUrl(),
            p.getRepoUrl(),
            p.getStartDate(),
            p.getEndDate(),
            p.getViewCount(),
            p.getEnterpriseRequests(),
            p.getField(),
            p.getMainTags() != null ? new java.util.ArrayList<>(p.getMainTags()) : new java.util.ArrayList<>(),
            p.getSubTags() != null ? new java.util.ArrayList<>(p.getSubTags()) : new java.util.ArrayList<>(),
            p.getSdgGoals() != null ? new java.util.ArrayList<>(p.getSdgGoals()) : new java.util.ArrayList<>(),
            p.getNst2Goals() != null ? new java.util.ArrayList<>(p.getNst2Goals()) : new java.util.ArrayList<>(),
            p.getAdditionalMediaUrls() != null ? new java.util.ArrayList<>(p.getAdditionalMediaUrls()) : new java.util.ArrayList<>(),
            p.getCreatedAt(),
            p.getUpdatedAt()
        );
        
        return ResponseEntity.status(HttpStatus.OK).body(projectDTO);
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<?> deleteProject(@PathVariable("id") Integer id) {
        Optional<Projects> project = service.getProjectById(id);
        if (project.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(java.util.Collections.singletonMap("message", "Project not found"));
        }
        service.deleteProject(id);
        return ResponseEntity.status(HttpStatus.OK).body(
                java.util.Collections.singletonMap("message", "Project " + project.get().getTitle() + " deleted"));
    }

    @PostMapping("/view/{id}")
    public ResponseEntity<?> incrementViewCount(@PathVariable("id") Integer id) {
        service.incrementViewCount(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
