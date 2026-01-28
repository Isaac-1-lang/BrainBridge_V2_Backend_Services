package com.learn.brainbridge.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;

/**
 * User Entity - Represents a user in the BrainBridge system
 * 
 * CONCEPTS TO LEARN:
 * 1. @Entity - Marks this class as a JPA entity (database table)
 * 2. @Table - Specifies the table name in the database
 * 3. @Id - Marks the primary key field
 * 4. @GeneratedValue - Auto-generates the ID value
 * 5. @Column - Customizes column properties (name, nullable, unique, etc.)
 * 6. @ManyToOne - Many users belong to one organization
 * 7. @PrePersist - Executes before entity is saved for the first time
 * 8. @PreUpdate - Executes before entity is updated
 * 9. Lombok annotations (@Data, @NoArgsConstructor, @AllArgsConstructor) - Reduces boilerplate
 */
@Entity
@Table(name = "users") // Table name in database
@Data // Lombok: Generates getters, setters, toString, equals, hashCode
@NoArgsConstructor // Lombok: Generates no-args constructor
@AllArgsConstructor // Lombok: Generates constructor with all fields
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment ID
    private Long id;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(nullable = false, unique = true, length = 255)
    private String username;

    @Column(name = "password_hash", nullable = false, length = 255)
    @JsonIgnore
    private String passwordHash;

    @Column(name = "first_name", length = 255)
    private String firstName;

    @Column(name = "last_name", length = 255)
    private String lastName;

    @Column(name = "profile_image_url", columnDefinition = "TEXT")
    private String profileImageUrl;

    @Column(length = 50)
    private String phone;

    @Column(columnDefinition = "TEXT")
    private String biography;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id")
    @JsonIgnore
    private Organization organization;

    @Column(name = "is_active")
    private Boolean isActive = true; // Default value

    @Column(name = "is_email_verified")
    private Boolean isEmailVerified = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * @PrePersist - Executes before entity is saved for the first time
     * Sets createdAt timestamp automatically
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    /**
     * @PreUpdate - Executes before entity is updated
     * Updates the updatedAt timestamp automatically
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
