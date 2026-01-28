package com.learn.brainbridge.service;

import com.learn.brainbridge.dtos.AnalyticsDTO;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

/**
 * AnalyticsService Interface - Defines business logic operations for Analytics
 */
@Service
public interface AnalyticsService {
    
    /**
     * Create a new analytics record
     * @param analyticsDTO Analytics data
     * @return Created analytics DTO
     */
    AnalyticsDTO createAnalytics(AnalyticsDTO analyticsDTO);
    
    /**
     * Get analytics by ID
     * @param id Analytics ID
     * @return Analytics DTO
     */
    AnalyticsDTO getAnalyticsById(Long id);
    
    /**
     * Get all analytics for a project
     * @param projectId Project ID
     * @return List of analytics DTOs
     */
    List<AnalyticsDTO> getAnalyticsByProjectId(Long projectId);
    
    /**
     * Get analytics summary for a project
     * @param projectId Project ID
     * @return Map of event type to count
     */
    Map<String, Long> getAnalyticsSummary(Long projectId);
    
    /**
     * Track a view event
     * @param projectId Project ID
     * @param ipAddress IP address
     * @param userAgent User agent string
     */
    void trackView(Long projectId, String ipAddress, String userAgent);
    
    /**
     * Track a like event
     * @param projectId Project ID
     */
    void trackLike(Long projectId);
    
    /**
     * Track a comment event
     * @param projectId Project ID
     */
    void trackComment(Long projectId);
}

