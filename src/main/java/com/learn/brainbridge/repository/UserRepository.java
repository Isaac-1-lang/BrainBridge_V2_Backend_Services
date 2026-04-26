package com.learn.brainbridge.repository;

import com.learn.brainbridge.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * UserRepository - Data Access Layer for User entity
 * 
 * CONCEPTS TO LEARN:
 * 1. JpaRepository<Entity, ID> - Provides CRUD operations automatically
 *    - save(), findById(), findAll(), delete(), etc.
 * 2. @Repository - Marks this as a Spring repository component
 * 3. Method naming conventions - Spring automatically creates queries from method names
 *    - findByEmail() becomes: SELECT * FROM users WHERE email = ?
 *    - findByEmailAndIsActive() becomes: SELECT * FROM users WHERE email = ? AND is_active = ?
 * 4. Optional<T> - Prevents NullPointerException, represents value that may or may not exist
 * 5. @Query - Custom SQL/JPQL queries
 * 6. @Param - Binds method parameter to query parameter
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * Spring automatically generates this query:
     * SELECT * FROM users WHERE email = ?
     * 
     * Method naming pattern: findBy + FieldName
     */
    Optional<User> findByEmail(String email);

    /**
     * Spring automatically generates this query:
     * SELECT * FROM users WHERE username = ?
     */
    Optional<User> findByUsername(String username);

    /**
     * Spring automatically generates this query:
     * SELECT * FROM users WHERE email = ? AND is_active = ?
     * 
     * Method naming pattern: findBy + Field1 + And + Field2
     */
    Optional<User> findByEmailAndIsActive(String email, Boolean isActive);

    /**
     * Custom query using JPQL (Java Persistence Query Language)
     * This is more flexible than method naming conventions
     * 
     * :email is a named parameter that gets replaced with the method parameter
     */
    @Query("SELECT u FROM User u WHERE u.email = :email AND u.isActive = true")
    Optional<User> findActiveUserByEmail(@Param("email") String email);

    /**
     * Check if a user exists with the given email
     * Spring generates: SELECT COUNT(*) > 0 FROM users WHERE email = ?
     */
    boolean existsByEmail(String email);

    /**
     * Check if a user exists with the given username
     */
    boolean existsByUsername(String username);
}
