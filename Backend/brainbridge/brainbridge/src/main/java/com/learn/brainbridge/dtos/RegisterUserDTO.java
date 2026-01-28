package com.learn.brainbridge.dtos;

import com.learn.brainbridge.annotations.PasswordMatches;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * RegisterUserDTO - DTO for user registration
 * Contains password field which is not in regular UserDTO
 */
@Schema(description = "User registration data")
@Data
@NoArgsConstructor
@AllArgsConstructor
@PasswordMatches
public class RegisterUserDTO {
    
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;
    
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;
    
    @NotBlank(message = "Password is required")
    // I might need to add more constraints like uppercase, lowercase, numbers, special characters, etc.
    @Size(min = 8, message = "Password must be at least 8 characters")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", message = "Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character")
    private String password;

    @NotBlank(message = "Confirm password is required")
    @Size(message = "Confirm password must be at least 8 characters")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", message = "Confirm password must contain at least one uppercase letter, one lowercase letter, one number, and one special character")
    private String confirmPassword;

    
    private String firstName;
    private String lastName;
    private String phone;
    private String biography;
    @NotBlank(message="Organization name is required")
    private String organizationName;

}

