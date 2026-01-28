package com.learn.brainbridge.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * ProjectDTO - Data Transfer Object for Project
 * 
 * CONCEPTS TO LEARN:
 * 1. DTO Pattern - Separates API layer from database layer
 * 2. @NotBlank - Validation: Field cannot be null, empty, or whitespace
 * 3. @Size - Validation: Limits string length
 * 4. DTOs don't include entity relationships directly (no User object, just userId)
 */
@Schema(description = "Project data transfer object")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDTO {

    private Long id;

    @NotBlank(message = "Title is required")
    @Size(min = 1, max = 200, message = "Title must be between 1 and 200 characters")
    private String title;

    @Size(max = 5000, message = "Description cannot exceed 5000 characters")
    private String description;

    /**
     * Array of programming languages used in the project
     * Maximum 10 languages allowed
     */
    @Schema(description = "Array of programming languages", example = "[\"Java\", \"JavaScript\"]", type = "array")
    private String[] programmingLanguage;

    private Boolean isPublic = true;

    private Boolean isActive = true;

    private Long viewCount = 0L;

    private Long likeCount = 0L;

    private Long userId; // Reference to user, not the full User object

    private String username; // For display purposes

    private Integer commentCount; // Number of comments

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
