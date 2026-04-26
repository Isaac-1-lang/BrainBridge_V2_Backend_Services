package com.learn.brainbridge.service;

import com.learn.brainbridge.dtos.LoginDTO;
import com.learn.brainbridge.dtos.RegisterUserDTO;
import com.learn.brainbridge.dtos.UserDTO;
import com.learn.brainbridge.generics.ApiResponses1;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import org.springframework.stereotype.Service;

/**
 * UserService Interface - Defines business logic operations
 * 
 * CONCEPTS TO LEARN:
 * 1. Service Layer Pattern - Separates business logic from controllers
 * 2. Interface-based design - Allows for different implementations
 * 3. Service methods represent business operations, not database operations
 */
@Service
public interface UserService {
    
    /**
     * Register a new user
     * @param registerDTO User registration data
     * @return Created user DTO
     */
    ApiResponses1<UserDTO> registerUser(RegisterUserDTO registerDTO,MultipartFile profileImage);
    
    /**
     * Login a user
     * @param loginDTO Login credentials
     * @return User DTO if login successful
     */
    UserDTO loginUser(LoginDTO loginDTO);
    
    /**
     * Get user by ID
     * @param id User ID
     * @return User DTO
     */
    UserDTO getUserById(Long id);
    
    /**
     * Get all users
     * @return List of user DTOs
     */
    List<UserDTO> getAllUsers();
    
    /**
     * Update user information
     * @param id User ID
     * @param userDTO Updated user data
     * @return Updated user DTO
     */
    UserDTO updateUser(Long id, UserDTO userDTO);
    
    /**
     * Delete user by ID
     * @param id User ID
     */
    void deleteUser(Long id);
}
