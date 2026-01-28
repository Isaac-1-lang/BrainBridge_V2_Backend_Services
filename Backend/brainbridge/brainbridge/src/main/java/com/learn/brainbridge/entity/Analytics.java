package com.learn.brainbridge.entity;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.learn.brainbridge.enums.AnalyticsEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Analytics Entity - Tracks analytics events in the BrainBridge system
 * 
 * CONCEPTS TO LEARN:
 * 1. @ManyToOne - Many analytics records belong to one user (optional)
 * 2. Analytics tracking - Records various events with entity type and context
 * 3. JSONB column - Stores flexible context data as JSON
 * 4. @Hidden - Prevents Swagger from including this entity in API documentation
 */
@Hidden
@Entity
@Table(name = "analytics_events")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Analytics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @Column(name = "event_type", nullable = false, length = 100)
    private String eventType;

    @Enumerated(EnumType.STRING)
    @Column(name = "entity_type")
    private AnalyticsEntity entityType;

    @Column(name = "entity_id")
    private Long entityId;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> context;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
