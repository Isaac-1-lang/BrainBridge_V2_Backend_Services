package com.learn.brainbridge.controllers;

import com.learn.brainbridge.dtos.CommentDTO;
import com.learn.brainbridge.service.CommentService;
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
 * CommentController - REST API Controller for Comment operations
 */
@RestController
@RequestMapping("/api/comments")
@Tag(name = "Comments", description = "API endpoints for managing project comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    /**
     * POST /api/comments
     * Create a new comment
     */
    @PostMapping
    @Operation(
            summary = "Create a new comment",
            description = "Creates a new comment on a project. The comment will be associated with the authenticated user."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Comment created successfully",
                    content = @Content(schema = @Schema(implementation = CommentDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Project or User not found")
    })
    public ResponseEntity<CommentDTO> createComment(
            @Valid @RequestBody CommentDTO commentDTO,
            @Parameter(description = "User ID of the comment creator", required = true)
            @RequestParam Long userId) {
        CommentDTO createdComment = commentService.createComment(commentDTO, userId);
        return new ResponseEntity<>(createdComment, HttpStatus.CREATED);
    }

    /**
     * GET /api/comments/{id}
     * Get comment by ID
     */
    @GetMapping("/{id}")
    @Operation(
            summary = "Get comment by ID",
            description = "Retrieves a comment by its ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment found",
                    content = @Content(schema = @Schema(implementation = CommentDTO.class))),
            @ApiResponse(responseCode = "404", description = "Comment not found")
    })
    public ResponseEntity<CommentDTO> getCommentById(
            @Parameter(description = "Comment ID", required = true)
            @PathVariable Long id) {
        CommentDTO comment = commentService.getCommentById(id);
        return ResponseEntity.ok(comment);
    }

    /**
     * GET /api/comments/project/{projectId}
     * Get all comments for a project
     */
    @GetMapping("/project/{projectId}")
    @Operation(
            summary = "Get comments by project ID",
            description = "Retrieves all comments for a specific project, ordered by creation date (newest first)."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comments retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Project not found")
    })
    public ResponseEntity<List<CommentDTO>> getCommentsByProjectId(
            @Parameter(description = "Project ID", required = true)
            @PathVariable Long projectId) {
        List<CommentDTO> comments = commentService.getCommentsByProjectId(projectId);
        return ResponseEntity.ok(comments);
    }

    /**
     * GET /api/comments/user/{userId}
     * Get all comments by user
     */
    @GetMapping("/user/{userId}")
    @Operation(
            summary = "Get comments by user ID",
            description = "Retrieves all comments created by a specific user."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comments retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<List<CommentDTO>> getCommentsByUserId(
            @Parameter(description = "User ID", required = true)
            @PathVariable Long userId) {
        List<CommentDTO> comments = commentService.getCommentsByUserId(userId);
        return ResponseEntity.ok(comments);
    }

    /**
     * PUT /api/comments/{id}
     * Update comment
     */
    @PutMapping("/{id}")
    @Operation(
            summary = "Update comment",
            description = "Updates an existing comment. Only the comment owner can update it."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment updated successfully",
                    content = @Content(schema = @Schema(implementation = CommentDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input or unauthorized"),
            @ApiResponse(responseCode = "404", description = "Comment not found")
    })
    public ResponseEntity<CommentDTO> updateComment(
            @Parameter(description = "Comment ID", required = true)
            @PathVariable Long id,
            @Valid @RequestBody CommentDTO commentDTO,
            @Parameter(description = "User ID for authorization", required = true)
            @RequestParam Long userId) {
        CommentDTO updatedComment = commentService.updateComment(id, commentDTO, userId);
        return ResponseEntity.ok(updatedComment);
    }

    /**
     * DELETE /api/comments/{id}
     * Delete comment
     */
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete comment",
            description = "Deletes a comment. Only the comment owner can delete it."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Comment deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Comment not found")
    })
    public ResponseEntity<Void> deleteComment(
            @Parameter(description = "Comment ID", required = true)
            @PathVariable Long id,
            @Parameter(description = "User ID for authorization", required = true)
            @RequestParam Long userId) {
        commentService.deleteComment(id, userId);
        return ResponseEntity.noContent().build();
    }
}
