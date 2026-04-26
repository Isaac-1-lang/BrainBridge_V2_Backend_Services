package com.learn.brainbridge.serviceImpl;

import com.learn.brainbridge.Exception.BadRequestException;
import com.learn.brainbridge.Exception.ResourceNotFoundException;
import com.learn.brainbridge.dtos.LoginDTO;
import com.learn.brainbridge.dtos.RegisterUserDTO;
import com.learn.brainbridge.dtos.UserDTO;
import com.learn.brainbridge.entity.User;
import com.learn.brainbridge.generics.ApiResponses1;
import com.learn.brainbridge.repository.UserRepository;
import com.learn.brainbridge.service.EmailVerificationService;
import com.learn.brainbridge.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

/**
 * UserServiceImpl - Implementation of UserService
 * 
 * CONCEPTS TO LEARN:
 * 1. @Service - Marks this class as a Spring service component
 *    - Spring automatically creates an instance (bean) of this class
 *    - Can be injected into other classes using @Autowired
 * 2. @Autowired - Dependency Injection
 *    - Spring automatically provides the UserRepository instance
 *    - No need to manually create: UserRepository repo = new UserRepository()
 * 3. Service Layer Responsibilities:
 *    - Business logic validation
 *    - Data transformation (Entity <-> DTO)
 *    - Exception handling
 *    - Transaction management
 */
@Service
public class UserServiceImpl implements UserService {

    /**
     * @Autowired - Spring injects UserRepository automatically
     * This is called "Dependency Injection" or "Inversion of Control"
     * Spring manages object creation and dependencies
     */
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailVerificationService emailVerificationService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Register a new user
     * Business logic:
     * 1. Check if email/username already exists
     * 2. Create new user entity
     * 3. Save to database
     * 4. Convert to DTO and return
     */
    @Override
    public ApiResponses1<UserDTO>  registerUser(RegisterUserDTO registerDTO,MultipartFile profileImage) {
        // Business validation: Check if email already exists
        if (userRepository.existsByEmail(registerDTO.getEmail())) {
            throw new BadRequestException("Email already exists: " + registerDTO.getEmail());
            
        }
        // Business validation: Check if username already exists
        if (userRepository.existsByUsername(registerDTO.getUsername())) {
            throw new BadRequestException("Username already exists: " + registerDTO.getUsername());
        }
        
        
        
        // Convert DTO to Entity
        User user = new User();
        user.setEmail(registerDTO.getEmail());
        user.setUsername(registerDTO.getUsername());
        user.setPasswordHash(passwordEncoder.encode(registerDTO.getPassword()));
        user.setFirstName(registerDTO.getFirstName());
        user.setLastName(registerDTO.getLastName());
        user.setIsActive(true);
        user.setIsEmailVerified(false);
        
        // Save to database (JPA automatically handles the insert)
        User savedUser = userRepository.save(user);

        // send email verification (best-effort; failures should be visible in logs)
        try {
            emailVerificationService.sendVerificationEmail(savedUser);
        } catch (Exception e) {
            // log and continue; user can still log in but will not be verified
            e.printStackTrace();
        }

        UserDTO userDTO  = convertToDTO(savedUser);
        
        // Convert Entity to DTO and return
        return new ApiResponses1<>(true,"User registered successfully",userDTO);
    }

    /**
     * Login a user
     * Business logic:
     * 1. Find user by email or username
     * 2. Verify password (in production, use password hashing!)
     * 3. Return user DTO
     */
    @Override
    public UserDTO loginUser(LoginDTO loginDTO) {
        // Try to find by email first
        User user = userRepository.findByEmail(loginDTO.getEmailOrUsername())
                .orElse(null);
        
        // If not found by email, try username
        if (user == null) {
            user = userRepository.findByUsername(loginDTO.getEmailOrUsername())
                    .orElseThrow(() -> new BadRequestException("Invalid email/username or password"));
        }
        
        // Verify password (in production, use BCrypt or similar!)
        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPasswordHash())) {
            throw new BadRequestException("Invalid email/username or password");
        }
        
        // Check if user is active
        if (!user.getIsActive()) {
            throw new BadRequestException("User account is deactivated");
        }
        
        return convertToDTO(user);
    }

    @Override
    public UserDTO getUserById(Long id) {
        // findById returns Optional<User>
        // orElseThrow throws exception if not found
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
        
        return convertToDTO(user);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        // findAll() returns List<User>
        // We convert each User to UserDTO using stream
        return userRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        // Find existing user
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
        
        // Update fields (only non-null fields from DTO)
        if (userDTO.getFirstName() != null) {
            user.setFirstName(userDTO.getFirstName());
        }
        if (userDTO.getLastName() != null) {
            user.setLastName(userDTO.getLastName());
        }
        if (userDTO.getProfileImageUrl() != null) {
            user.setProfileImageUrl(userDTO.getProfileImageUrl());
        }
        
        // Save updated user
        User updatedUser = userRepository.save(user);
        
        return convertToDTO(updatedUser);
    }

    @Override
    public void deleteUser(Long id) {
        // Check if user exists
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User", id);
        }
        
        // Delete user
        userRepository.deleteById(id);
    }

    /**
     * Helper method to convert Entity to DTO
     * This separates internal entity structure from API response
     */
    private UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setUsername(user.getUsername());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setProfileImageUrl(user.getProfileImageUrl());
        dto.setIsActive(user.getIsActive());
        dto.setIsEmailVerified(user.getIsEmailVerified());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        return dto;
    }
}

