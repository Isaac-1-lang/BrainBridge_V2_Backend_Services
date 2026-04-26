# Registration API Flow - Complete Guide

## 📋 Overview
This document explains the **complete flow** of the Registration API from the moment a request is received until a response is sent back.

---

## 🔄 Complete Request Flow Diagram

```
[Frontend/Client] 
    ↓ POST /api/users/register + JSON Body
[Spring Boot DispatcherServlet] 
    ↓ Routes to Controller
[UserController] 
    ↓ Validates DTO + Calls Service
[UserService (Interface)] 
    ↓ Implemented by
[UserServiceImpl] 
    ↓ Business Logic + Calls Repository
[UserRepository (JPA)] 
    ↓ Database Query
[Database (MySQL/PostgreSQL)] 
    ↓ Returns Entity
[User Entity] 
    ↓ Convert to DTO
[UserDTO] 
    ↓ Returns to Controller
[ResponseEntity<UserDTO>] 
    ↓ JSON Response
[Frontend/Client]
```

---

## 📦 Step-by-Step Breakdown

### **STEP 1: Request Arrives at Controller**
**File:** `UserController.java`

```java
@PostMapping("/register")
public ResponseEntity<UserDTO> registerUser(@Valid @RequestBody RegisterUserDTO registerDTO) {
    UserDTO createdUser = userService.registerUser(registerDTO);
    return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
}
```

**What happens:**
1. **`@PostMapping("/register")`** - Spring routes `POST /api/users/register` to this method
2. **`@RequestBody RegisterUserDTO`** - Spring converts JSON request body to Java object
   ```json
   {
     "email": "ingenziacquilas@gmail.com",
     "username": "Acquilas",
     "password": "Password123!",
     "confirmPassword": "Password123!",
     "firstName": "NIYOBYOSE",
     "lastName": "Isaac Precieux"
   }
   ```
3. **`@Valid`** - Triggers validation on `RegisterUserDTO` fields
   - Checks `@NotBlank`, `@Email`, `@Size`, `@Pattern` annotations
   - If validation fails → Returns HTTP 400 (Bad Request) automatically

**Key Concepts:**
- `@RestController` - Combines `@Controller` + `@ResponseBody` (auto JSON conversion)
- `@RequestMapping("/api/users")` - Base path for all endpoints in this controller
- `ResponseEntity<T>` - Wraps response with HTTP status code

---

### **STEP 2: Validation in DTO**
**File:** `RegisterUserDTO.java`

```java
@NotBlank(message = "Email is required")
@Email(message = "Email should be valid")
private String email;

@NotBlank(message = "Username is required")
@Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
private String username;

@NotBlank(message = "Password is required")
@Size(min = 8, message = "Password must be at least 8 characters")
@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$")
private String password;

@PasswordMatches // Custom validation annotation
private String confirmPassword;
```

**What happens:**
- Spring validates each field before entering the controller method
- If validation fails:
  ```json
  {
    "status": 400,
    "message": "Validation failed",
    "errors": [
      "Email is required",
      "Password must be at least 8 characters"
    ]
  }
  ```

**Key Concepts:**
- **DTO (Data Transfer Object)** - Separates API layer from entity layer
- **Bean Validation** - `@Valid` triggers Jakarta Validation framework
- **Custom Validators** - `@PasswordMatches` checks password == confirmPassword

---

### **STEP 3: Controller Calls Service**
**File:** `UserController.java` → `UserServiceImpl.java`

```java
// In Controller
UserDTO createdUser = userService.registerUser(registerDTO);

// This calls the Service Interface
public interface UserService {
    UserDTO registerUser(RegisterUserDTO registerDTO);
}
```

**What happens:**
- Controller doesn't know about database/repository
- It only knows about the Service interface (abstraction)
- **Dependency Injection**: `@Autowired` injects `UserServiceImpl` automatically

**Key Concepts:**
- **Layered Architecture**: Controller → Service → Repository
- **Dependency Injection (DI)**: Spring creates and injects dependencies
- **Interface-based design**: Controller depends on interface, not implementation

---

### **STEP 4: Service Layer - Business Logic**
**File:** `UserServiceImpl.java`

```java
@Override
public UserDTO registerUser(RegisterUserDTO registerDTO) {
    // 1. Business validation: Check if email already exists
    if (userRepository.existsByEmail(registerDTO.getEmail())) {
        throw new BadRequestException("Email already exists: " + registerDTO.getEmail());
    }
    
    // 2. Business validation: Check if username already exists
    if (userRepository.existsByUsername(registerDTO.getUsername())) {
        throw new BadRequestException("Username already exists: " + registerDTO.getUsername());
    }
    
    // 3. Convert DTO to Entity
    User user = new User();
    user.setEmail(registerDTO.getEmail());
    user.setUsername(registerDTO.getUsername());
    user.setPassword(registerDTO.getPassword()); // TODO: Hash password in production!
    user.setFirstName(registerDTO.getFirstName());
    user.setLastName(registerDTO.getLastName());
    user.setIsActive(true);
    user.setIsEmailVerified(false);
    
    // 4. Save to database
    User savedUser = userRepository.save(user);
    
    // 5. Convert Entity to DTO and return
    return convertToDTO(savedUser);
}
```

**What happens:**
1. **Business Validation** - Checks if email/username already exists
   - This is different from DTO validation (which checks format)
   - This checks business rules (uniqueness)
2. **DTO → Entity Conversion** - Converts `RegisterUserDTO` to `User` entity
3. **Repository Save** - Saves user to database
4. **Entity → DTO Conversion** - Converts saved `User` entity to `UserDTO` for response

**Key Concepts:**
- **Service Layer Responsibilities:**
  - Business logic validation
  - Data transformation (DTO ↔ Entity)
  - Exception handling
  - Transaction management (by default, `@Service` methods are transactional)
- **Repository Injection**: `@Autowired` injects `UserRepository`

---

### **STEP 5: Repository Layer - Database Access**
**File:** `UserRepository.java`

```java
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    User save(User user); // Inherited from JpaRepository
}
```

**What happens:**
1. **`existsByEmail()`** - Spring generates query: `SELECT COUNT(*) > 0 FROM users WHERE email = ?`
2. **`existsByUsername()`** - Spring generates query: `SELECT COUNT(*) > 0 FROM users WHERE username = ?`
3. **`save()`** - JPA handles INSERT/UPDATE:
   - If user has ID → UPDATE
   - If user has no ID → INSERT

**Key Concepts:**
- **JpaRepository<Entity, ID>** - Provides CRUD operations:
  - `save()`, `findById()`, `findAll()`, `delete()`, etc.
- **Spring Data JPA Method Naming**:
  - `existsByEmail()` → `SELECT COUNT(*) > 0 FROM users WHERE email = ?`
  - `findByEmail()` → `SELECT * FROM users WHERE email = ?`
  - `findByEmailAndIsActive()` → `SELECT * FROM users WHERE email = ? AND is_active = ?`
- **@Repository** - Marks as data access component

---

### **STEP 6: Entity - Database Table Mapping**
**File:** `User.java`

```java
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true, length = 100)
    private String email;
    
    @Column(nullable = false)
    private String password;
    
    @Column(name = "first_name", length = 50)
    private String firstName;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
}
```

**What happens:**
1. **`@Entity`** - Marks class as JPA entity (maps to database table)
2. **`@Table(name = "users")`** - Specifies table name
3. **`@Id`** - Marks primary key
4. **`@GeneratedValue`** - Auto-increments ID
5. **`@Column`** - Maps field to column (can specify name, nullable, unique, length)
6. **`@PrePersist`** - Runs before first save (sets timestamps)

**Database Table Structure:**
```sql
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(100) NOT NULL UNIQUE,
    username VARCHAR(100) NOT NULL,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    is_active BOOLEAN DEFAULT true,
    is_email_verified BOOLEAN DEFAULT false,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);
```

**Key Concepts:**
- **JPA (Java Persistence API)** - ORM framework (maps Java objects to database tables)
- **ORM (Object-Relational Mapping)** - Converts Java objects to SQL automatically
- **Lifecycle Callbacks** - `@PrePersist`, `@PreUpdate` run at specific times

---

### **STEP 7: Entity → DTO Conversion**
**File:** `UserServiceImpl.java`

```java
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
    // Note: Password is NOT included in DTO (security)
    return dto;
}
```

**What happens:**
- Converts `User` entity to `UserDTO` for response
- **Excludes sensitive data** (password) from response
- Only sends what the client needs to see

**Why DTOs?**
- **Security**: Don't expose internal entity structure (passwords, internal IDs)
- **API Contract**: Clear API response format
- **Flexibility**: Different DTOs for different operations (RegisterUserDTO, LoginDTO, UserDTO)

---

### **STEP 8: Response Returned**
**File:** `UserController.java`

```java
return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
```

**Response JSON:**
```json
{
  "id": 1,
  "email": "user@example.com",
  "username": "johndoe",
  "firstName": "John",
  "lastName": "Doe",
  "profileImageUrl": null,
  "isActive": true,
  "isEmailVerified": false,
  "createdAt": "2025-01-15T10:30:00",
  "updatedAt": "2025-01-15T10:30:00"
}
```

**HTTP Response:**
- **Status Code:** `201 CREATED`
- **Headers:** `Content-Type: application/json`
- **Body:** `UserDTO` as JSON

---

## 🔑 Key Spring Boot Concepts

### 1. **Dependency Injection (DI) / Inversion of Control (IoC)**
```java
@Autowired
private UserService userService; // Spring creates and injects UserServiceImpl
```
- Spring creates and manages object instances (beans)
- No need to use `new` keyword
- Dependencies are injected automatically

### 2. **Layered Architecture**
```
Controller (Handles HTTP requests/responses)
    ↓
Service (Business logic)
    ↓
Repository (Data access)
    ↓
Database
```

### 3. **Annotations Explained**
- `@RestController` - Combines `@Controller` + `@ResponseBody`
- `@Service` - Marks business logic component
- `@Repository` - Marks data access component
- `@Entity` - Marks database table mapping
- `@Autowired` - Dependency injection
- `@Valid` - Triggers validation
- `@RequestMapping` - Maps URL patterns

### 4. **JPA Repository Methods**
Spring generates SQL from method names:
```java
// Method name → Generated SQL
existsByEmail(String email) → SELECT COUNT(*) > 0 FROM users WHERE email = ?
findByEmail(String email) → SELECT * FROM users WHERE email = ?
findByEmailAndIsActive(String email, Boolean active) → SELECT * FROM users WHERE email = ? AND is_active = ?
```

---

## 🚨 Error Handling Flow

### Validation Errors (HTTP 400)
```java
@Valid @RequestBody RegisterUserDTO registerDTO
```
- If DTO validation fails → Spring returns 400 with validation errors

### Business Logic Errors (HTTP 400)
```java
if (userRepository.existsByEmail(registerDTO.getEmail())) {
    throw new BadRequestException("Email already exists");
}
```
- Service throws `BadRequestException` → Controller returns 400

### Not Found Errors (HTTP 404)
```java
User user = userRepository.findById(id)
    .orElseThrow(() -> new ResourceNotFoundException("User", id));
```
- Service throws `ResourceNotFoundException` → Controller returns 404

---

## 📚 Summary

**Registration API Flow:**
1. **Request** → POST `/api/users/register` + JSON body
2. **Controller** → Receives request, validates DTO, calls service
3. **Service** → Business validation, converts DTO→Entity, saves to DB
4. **Repository** → Database operations (check existence, save)
5. **Entity** → Maps to database table
6. **Service** → Converts Entity→DTO, returns
7. **Controller** → Wraps in ResponseEntity, returns JSON response

**Key Principles:**
- **Separation of Concerns**: Each layer has specific responsibility
- **Dependency Injection**: Spring manages dependencies
- **DTO Pattern**: Separates API from database structure
- **JPA/ORM**: Maps Java objects to database automatically
- **Validation**: Multiple levels (DTO, business logic)

---

## 🎯 Next Steps to Understand

1. **Security**: How to hash passwords (BCrypt)
2. **JWT Authentication**: How to generate tokens
3. **Exception Handling**: Global exception handler
4. **Transactions**: How `@Transactional` works
5. **Testing**: Unit tests for each layer
