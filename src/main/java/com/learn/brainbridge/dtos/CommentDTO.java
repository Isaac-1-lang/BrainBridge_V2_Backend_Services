package com.learn.brainbridge.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * CommentDTO - Data Transfer Object for Comment
 * 
 * CONCEPTS TO LEARN:
 * 1. @NotNull - Validation: Field cannot be null
 * 2. DTOs simplify complex entity relationships for API consumers
 */
@Schema(description = "Comment data transfer object")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {

    private Long id;

    @NotBlank(message = "Comment content is required")
    @Size(min = 1, max = 5000, message = "Comment must be between 1 and 5000 characters")
    private String content;

    @NotNull(message = "Project ID is required")
    private Long projectId;

    @NotNull(message = "User ID is required")
    private Long userId;

    private String username; // For display purposes

    private Boolean isEdited = false;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
