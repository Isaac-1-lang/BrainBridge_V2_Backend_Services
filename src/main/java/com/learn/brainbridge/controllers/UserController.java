package com.learn.brainbridge.controllers;

import com.learn.brainbridge.dtos.AuthResponseDTO;
import com.learn.brainbridge.dtos.LoginDTO;
import com.learn.brainbridge.dtos.RegisterUserDTO;
import com.learn.brainbridge.dtos.UserDTO;
import com.learn.brainbridge.entity.User;
import com.learn.brainbridge.generics.ApiResponses1;
import com.learn.brainbridge.service.EmailVerificationService;
import com.learn.brainbridge.service.UserService;
import com.learn.brainbridge.repository.UserRepository;
import com.learn.brainbridge.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * UserController - REST API Controller for User operations
 * 
 * CONCEPTS TO LEARN:
 * 1. @RestController - Combines @Controller + @ResponseBody
 *    - @Controller: Marks this as a Spring MVC controller
 *    - @ResponseBody: Converts return values to JSON automatically
 * 2. @RequestMapping - Base URL path for all endpoints in this controller
 * 3. @Autowired - Dependency injection (injects UserService)
 * 4. HTTP Methods:
 *    - @GetMapping: GET request (retrieve data)
 *    - @PostMapping: POST request (create data)
 *    - @PutMapping: PUT request (update data)
 *    - @DeleteMapping: DELETE request (delete data)
 * 5. @PathVariable - Extracts value from URL path (/api/users/{id})
 * 6. @RequestBody - Converts JSON request body to Java object
 * 7. @Valid - Triggers validation on DTO (checks @NotBlank, @Email, etc.)
 * 8. ResponseEntity<T> - Wraps response with HTTP status code
 * 9. HttpStatus - HTTP status codes (OK, CREATED, NOT_FOUND, etc.)
 */
@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "API endpoints for managing users and authentication")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;

    @Autowired
    private EmailVerificationService emailVerificationService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private com.learn.brainbridge.service.CloudinaryService cloudinaryService;

    /**
     * POST /api/users/register
     * Register a new user
     * 
     * @param registerDTO - User registration data from request body
     * @return ResponseEntity with created user and HTTP 201 CREATED status
     */
    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Creates a new user account with email, username, and password.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input or email/username already exists"),
            @ApiResponse(responseCode = "500", description="Internal Server Error")
    })
    public ResponseEntity<ApiResponses1<AuthResponseDTO>> registerUser(
            @Valid @RequestBody RegisterUserDTO registerUserDTO) {
        ApiResponses1<UserDTO> userResponse = userService.registerUser(registerUserDTO, null);
        UserDTO user = userResponse.getData();

        // Issue a JWT immediately so the frontend can call authenticated endpoints
        // (e.g. profile-picture upload) without requiring a separate login step
        String subject = (user.getEmail() != null && !user.getEmail().isEmpty())
                ? user.getEmail()
                : user.getUsername();
        String token = jwtUtil.generateToken(subject);

        AuthResponseDTO payload = new AuthResponseDTO(token, user);
        ApiResponses1<AuthResponseDTO> response = new ApiResponses1<>(true, "User registered successfully", payload);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/verify-otp")
    @Operation(summary = "Verify OTP", description = "Verifies a user's email using a 6-digit OTP code.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Email verified successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid or expired OTP")
    })
    public ResponseEntity<ApiResponses1<Void>> verifyOTP(
            @RequestBody java.util.Map<String, String> request) {
        String email = request.get("email");
        String otp = request.get("otp");
        
        emailVerificationService.verifyOTP(email, otp);
        ApiResponses1<Void> response = new ApiResponses1<>(true, "Email verified successfully", null);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/resend-otp")
    @Operation(summary = "Resend OTP", description = "Resends a new 6-digit OTP code to the user's email.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OTP sent successfully"),
            @ApiResponse(responseCode = "400", description = "Email already verified or user not found")
    })
    public ResponseEntity<ApiResponses1<Void>> resendOTP(
            @RequestBody java.util.Map<String, String> request) {
        String email = request.get("email");
        
        emailVerificationService.resendOTP(email);
        ApiResponses1<Void> response = new ApiResponses1<>(true, "Verification code sent to your email", null);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/verify-email")
    @Operation(summary = "Verify email (deprecated)", description = "Legacy endpoint for token-based verification. Use /verify-otp instead.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Email verified successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid or expired token")
    })
    @Deprecated
    public ResponseEntity<ApiResponses1<Void>> verifyEmail(@RequestParam("token") String token) {
        // This is kept for backward compatibility but should use OTP flow
        ApiResponses1<Void> response = new ApiResponses1<>(false, "Please use OTP verification instead", null);
        return ResponseEntity.badRequest().body(response);
    }
    
    @PostMapping("/login")
    @Operation(summary = "Login user", description = "Authenticates a user with email/username and password.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful",
                    content = @Content(schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(responseCode = "403", description = "Email not verified - OTP sent"),
            @ApiResponse(responseCode = "400", description = "Invalid credentials or account deactivated")
    })
    public ResponseEntity<ApiResponses1<AuthResponseDTO>> loginUser(@Valid @RequestBody LoginDTO loginDTO) {
        UserDTO user  = userService.loginUser(loginDTO);

        // Check if email is verified
        if (Boolean.FALSE.equals(user.getIsEmailVerified())) {
            // Send OTP for verification
            User userEntity = userRepository.findByEmail(user.getEmail())
                    .orElseGet(() -> userRepository.findByUsername(user.getUsername()).orElse(null));
            
            if (userEntity != null) {
                emailVerificationService.sendVerificationEmail(userEntity);
            }
            
            ApiResponses1<AuthResponseDTO> response = new ApiResponses1<>(
                false, 
                "Email not verified. Verification code sent to your email.", 
                null
            );
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }

        // Use email if present, otherwise username as JWT subject
        String subject = (user.getEmail() != null && !user.getEmail().isEmpty())
                ? user.getEmail()
                : user.getUsername();
        String token = jwtUtil.generateToken(subject);

        AuthResponseDTO payload = new AuthResponseDTO(token, user);
        ApiResponses1<AuthResponseDTO> response  = new ApiResponses1<>(true,"Logged in successfully", payload);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/users/me
     * Returns the currently authenticated user's profile.
     */
    @GetMapping("/me")
    public ResponseEntity<UserDTO> getMe(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String principalName = authentication.getName();

        // JWT subject is email if present, otherwise username (see loginUser).
        User user = userRepository.findByEmail(principalName)
                .orElseGet(() -> userRepository.findByUsername(principalName).orElse(null));

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(userService.getUserById(user.getId()));
    }
    

    /**
     * GET /api/users/{id}
     * Get user by ID
     * 
     * @param id - User ID from URL path
     * @return ResponseEntity with user data and HTTP 200 OK status
     */
    @GetMapping("/{id}")
    @Operation(
            summary = "Get user by ID",
            description = "Retrieves user information by user ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found",
                    content = @Content(schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<UserDTO> getUserById(
            @Parameter(description = "User ID", required = true)
            @PathVariable Long id) {
        UserDTO user = userService.getUserById(id);
        return ResponseEntity.ok(user); // Shortcut for new ResponseEntity<>(user, HttpStatus.OK)
    }

    /**
     * GET /api/users
     * Get all users
     * 
     * @return ResponseEntity with list of users and HTTP 200 OK status
     */
    @GetMapping
    @Operation(
            summary = "Get all users",
            description = "Retrieves a list of all users in the system."
    )
    @ApiResponse(responseCode = "200", description = "List of users retrieved successfully")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    /**
     * PUT /api/users/{id}
     * Update user information
     * 
     * @param id - User ID from URL path
     * @param userDTO - Updated user data from request body
     * @return ResponseEntity with updated user and HTTP 200 OK status
     */
    @PutMapping("/{id}")
    @Operation(
            summary = "Update user",
            description = "Updates user information (firstName, lastName, profileImageUrl)."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully",
                    content = @Content(schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<UserDTO> updateUser(
            @Parameter(description = "User ID", required = true)
            @PathVariable Long id,
            @Valid @RequestBody UserDTO userDTO) {
        UserDTO updatedUser = userService.updateUser(id, userDTO);
        return ResponseEntity.ok(updatedUser);
    }

    /**
     * POST /api/users/me/profile-picture
     * Upload profile picture for authenticated user
     */
    @PostMapping(value = "/me/profile-picture", consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Upload profile picture",
            description = "Uploads a profile picture for the authenticated user to Cloudinary."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile picture uploaded successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid file or file too large"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<?> uploadProfilePicture(
            @RequestParam("file") org.springframework.web.multipart.MultipartFile file,
            Authentication authentication) {
        
        log.info("=== Profile Picture Upload Request ===");
        log.info("  Authentication: {}", authentication);
        log.info("  Principal: {}", authentication != null ? authentication.getName() : "NULL");
        log.info("  File: {}", file != null ? file.getOriginalFilename() + " (" + file.getSize() + " bytes)" : "NULL");

        if (authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getName())) {
            log.warn("  REJECTED: No valid authentication. Principal={}",
                    authentication != null ? authentication.getName() : "NULL");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(java.util.Map.of("error", "Unauthorized - please log in"));
        }

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(java.util.Map.of("error", "Please select a file to upload."));
        }

        // Validate file type
        String contentType = file.getContentType();
        if (contentType == null || (!contentType.startsWith("image/"))) {
            return ResponseEntity.badRequest().body(java.util.Map.of("error", "Only image files are allowed."));
        }

        // Validate file size (max 5MB)
        if (file.getSize() > 5 * 1024 * 1024) {
            return ResponseEntity.badRequest().body(java.util.Map.of("error", "File size must be less than 5MB."));
        }

        try {
            String principalName = authentication.getName();
            User user = userRepository.findByEmail(principalName)
                    .orElseGet(() -> userRepository.findByUsername(principalName).orElse(null));

            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }

            // Upload to Cloudinary
            String imageUrl = cloudinaryService.uploadImage(file, "profile_pictures");
            
            // Update user profile
            user.setProfileImageUrl(imageUrl);
            userRepository.save(user);

            return ResponseEntity.ok(java.util.Map.of(
                    "url", imageUrl,
                    "message", "Profile picture uploaded successfully"
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(java.util.Map.of("error", "Error uploading file: " + e.getMessage()));
        }
    }

    /**
     * DELETE /api/users/{id}
     * Delete user by ID
     * 
     * @param id - User ID from URL path
     * @return ResponseEntity with HTTP 204 NO_CONTENT status
     */
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete user",
            description = "Permanently deletes a user account."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "User ID", required = true)
            @PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build(); // HTTP 204 No Content
        // 529 is the one which has completely stopped receiving requests
        // 429 shows that there is rate limiting the API is actually receiving the requests but it can't continue processing them.
    }
}
