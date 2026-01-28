package com.learn.brainbridge.repository;

import com.learn.brainbridge.entity.Analytics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * AnalyticsRepository - Data Access Layer for Analytics entity
 */
@Repository
public interface AnalyticsRepository extends JpaRepository<Analytics, Long> {
    
    /**
     * Find all analytics for a project
     * Uses project.id to query by project ID
     */
    @Query("SELECT a FROM Analytics a WHERE a.project.id = :projectId")
    List<Analytics> findByProjectId(@Param("projectId") Long projectId);
    
    /**
     * Find analytics by event type
     */
    List<Analytics> findByEventType(String eventType);
    
    /**
     * Find analytics for a project by event type
     */
    @Query("SELECT a FROM Analytics a WHERE a.project.id = :projectId AND a.eventType = :eventType")
    List<Analytics> findByProjectIdAndEventType(@Param("projectId") Long projectId, @Param("eventType") String eventType);
    
    /**
     * Count analytics events for a project
     */
    @Query("SELECT COUNT(a) FROM Analytics a WHERE a.project.id = :projectId")
    long countByProjectId(@Param("projectId") Long projectId);
    
    /**
     * Count analytics events for a project by event type
     */
    @Query("SELECT COUNT(a) FROM Analytics a WHERE a.project.id = :projectId AND a.eventType = :eventType")
    long countByProjectIdAndEventType(@Param("projectId") Long projectId, @Param("eventType") String eventType);
    
    /**
     * Find analytics within a date range
     */
    @Query("SELECT a FROM Analytics a WHERE a.project.id = :projectId AND a.createdAt BETWEEN :startDate AND :endDate")
    List<Analytics> findByProjectIdAndDateRange(
            @Param("projectId") Long projectId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
    
    /**
     * Get analytics summary for a project
     */
    @Query("SELECT a.eventType, COUNT(a) FROM Analytics a WHERE a.project.id = :projectId GROUP BY a.eventType")
    List<Object[]> getAnalyticsSummaryByProjectId(@Param("projectId") Long projectId);
}

