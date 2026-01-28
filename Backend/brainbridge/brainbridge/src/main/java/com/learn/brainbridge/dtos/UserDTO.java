package com.learn.brainbridge.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * UserDTO - Data Transfer Object for User
 * 
 * CONCEPTS TO LEARN:
 * 1. DTO Pattern - Separates API layer from entity layer
 *    - Prevents exposing internal entity structure
 *    - Allows different data for request vs response
 * 2. @NotBlank - Validates that field is not null, empty, or whitespace
 * 3. @Email - Validates email format
 * 4. @Size - Validates string length (min/max)
 * 5. Validation annotations are checked when @Valid is used in controller
 */
@Schema(description = "User data transfer object")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    
    private Long id;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;
    
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;
    
    // Note: Password is NOT included in DTO for security
    // We'll create separate DTOs for registration/login
    
    private String firstName;
    private String lastName;
    private String profileImageUrl;
    private Boolean isActive;
    private Boolean isEmailVerified;
    private String Bio;
    private String Organization_name;
    private LocalDateTime lastLogin;
    private LocalDateTime deActivated;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
