package com.learn.brainbridge.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * LoginDTO - DTO for user login
 */
@Schema(description = "Login credentials")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginDTO {
    
    @NotBlank(message = "Email or username is required")
    private String emailOrUsername;
    
    @NotBlank(message = "Password is required")
    private String password;

    public String getEmailOrUsername() {
        return emailOrUsername;
    }

    public void setEmailOrUsername(String emailOrUsername) {
        this.emailOrUsername = emailOrUsername;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

