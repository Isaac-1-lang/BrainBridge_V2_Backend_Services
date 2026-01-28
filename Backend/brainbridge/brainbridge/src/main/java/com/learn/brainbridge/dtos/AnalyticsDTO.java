package com.learn.brainbridge.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * AnalyticsDTO - Data Transfer Object for Analytics
 * 
 * CONCEPTS TO LEARN:
 * 1. Analytics DTOs are used for tracking and reporting
 * 2. Event types: VIEW, LIKE, COMMENT, SHARE, etc.
 */
@Schema(description = "Analytics data transfer object")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnalyticsDTO {

    private Long id;

    @NotNull(message = "Project ID is required")
    private Long projectId;

    @NotBlank(message = "Event type is required")
    private String eventType; // VIEW, LIKE, COMMENT, SHARE, etc.

    private String eventData; // JSON string for additional data

    private String ipAddress;

    private String userAgent;

    private LocalDateTime createdAt;
}
