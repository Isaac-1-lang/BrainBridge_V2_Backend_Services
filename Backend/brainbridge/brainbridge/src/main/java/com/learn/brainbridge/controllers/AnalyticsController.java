package com.learn.brainbridge.controllers;

import com.learn.brainbridge.dtos.AnalyticsDTO;
import com.learn.brainbridge.service.AnalyticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * AnalyticsController - REST API Controller for Analytics operations
 */
@RestController
@RequestMapping("/api/analytics")
@Tag(name = "Analytics", description = "API endpoints for tracking and viewing analytics data")
public class AnalyticsController {

    @Autowired
    private AnalyticsService analyticsService;

    /**
     * POST /api/analytics
     * Create a new analytics record
     */
    @PostMapping
    @Operation(
            summary = "Create analytics record",
            description = "Creates a new analytics record for tracking events."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Analytics record created successfully",
                    content = @Content(schema = @Schema(implementation = AnalyticsDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Project not found")
    })
    public ResponseEntity<AnalyticsDTO> createAnalytics(@Valid @RequestBody AnalyticsDTO analyticsDTO) {
        AnalyticsDTO createdAnalytics = analyticsService.createAnalytics(analyticsDTO);
        return new ResponseEntity<>(createdAnalytics, HttpStatus.CREATED);
    }

    /**
     * GET /api/analytics/{id}
     * Get analytics by ID
     */
    @GetMapping("/{id}")
    @Operation(
            summary = "Get analytics record by ID",
            description = "Retrieves a specific analytics record by its ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Analytics record found",
                    content = @Content(schema = @Schema(implementation = AnalyticsDTO.class))),
            @ApiResponse(responseCode = "404", description = "Analytics record not found")
    })
    public ResponseEntity<AnalyticsDTO> getAnalyticsById(
            @Parameter(description = "Analytics ID", required = true)
            @PathVariable Long id) {
        AnalyticsDTO analytics = analyticsService.getAnalyticsById(id);
        return ResponseEntity.ok(analytics);
    }

    /**
     * GET /api/analytics/project/{projectId}
     * Get all analytics for a project
     */
    @GetMapping("/project/{projectId}")
    @Operation(
            summary = "Get analytics by project ID",
            description = "Retrieves all analytics records for a specific project."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Analytics records retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Project not found")
    })
    public ResponseEntity<List<AnalyticsDTO>> getAnalyticsByProjectId(
            @Parameter(description = "Project ID", required = true)
            @PathVariable Long projectId) {
        List<AnalyticsDTO> analytics = analyticsService.getAnalyticsByProjectId(projectId);
        return ResponseEntity.ok(analytics);
    }

    /**
     * GET /api/analytics/project/{projectId}/summary
     * Get analytics summary for a project
     */
    @GetMapping("/project/{projectId}/summary")
    @Operation(
            summary = "Get analytics summary",
            description = "Retrieves a summary of analytics data for a project, grouped by event type."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Analytics summary retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Project not found")
    })
    public ResponseEntity<Map<String, Long>> getAnalyticsSummary(
            @Parameter(description = "Project ID", required = true)
            @PathVariable Long projectId) {
        Map<String, Long> summary = analyticsService.getAnalyticsSummary(projectId);
        return ResponseEntity.ok(summary);
    }

    /**
     * POST /api/analytics/project/{projectId}/track/view
     * Track a view event
     */
    @PostMapping("/project/{projectId}/track/view")
    @Operation(
            summary = "Track view event",
            description = "Records a view event for a project. Automatically captures IP address and user agent."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "View event tracked successfully"),
            @ApiResponse(responseCode = "404", description = "Project not found")
    })
    public ResponseEntity<Void> trackView(
            @Parameter(description = "Project ID", required = true)
            @PathVariable Long projectId,
            HttpServletRequest request) {
        String ipAddress = getClientIpAddress(request);
        String userAgent = request.getHeader("User-Agent");
        analyticsService.trackView(projectId, ipAddress, userAgent);
        return ResponseEntity.ok().build();
    }

    /**
     * POST /api/analytics/project/{projectId}/track/like
     * Track a like event
     */
    @PostMapping("/project/{projectId}/track/like")
    @Operation(
            summary = "Track like event",
            description = "Records a like event for a project."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Like event tracked successfully"),
            @ApiResponse(responseCode = "404", description = "Project not found")
    })
    public ResponseEntity<Void> trackLike(
            @Parameter(description = "Project ID", required = true)
            @PathVariable Long projectId) {
        analyticsService.trackLike(projectId);
        return ResponseEntity.ok().build();
    }

    /**
     * POST /api/analytics/project/{projectId}/track/comment
     * Track a comment event
     */
    @PostMapping("/project/{projectId}/track/comment")
    @Operation(
            summary = "Track comment event",
            description = "Records a comment event for a project."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment event tracked successfully"),
            @ApiResponse(responseCode = "404", description = "Project not found")
    })
    public ResponseEntity<Void> trackComment(
            @Parameter(description = "Project ID", required = true)
            @PathVariable Long projectId) {
        analyticsService.trackComment(projectId);
        return ResponseEntity.ok().build();
    }

    /**
     * Helper method to get client IP address
     */
    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }
        return request.getRemoteAddr();
    }
}
