package com.learn.brainbridge.service;

import com.learn.brainbridge.dtos.LoginDTO;
import com.learn.brainbridge.dtos.RegisterUserDTO;
import com.learn.brainbridge.dtos.UserDTO;

import java.util.List;

/**
 * UserService Interface - Defines business logic operations
 * 
 * CONCEPTS TO LEARN:
 * 1. Service Layer Pattern - Separates business logic from controllers
 * 2. Interface-based design - Allows for different implementations
 * 3. Service methods represent business operations, not database operations
 */
public interface UserService {
    
    /**
     * Register a new user
     * @param registerDTO User registration data
     * @return Created user DTO
     */
    UserDTO registerUser(RegisterUserDTO registerDTO);
    
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
