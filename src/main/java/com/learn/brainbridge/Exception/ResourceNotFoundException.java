package com.learn.brainbridge.Exception;

/**
 * ResourceNotFoundException - Custom exception for when a resource is not found
 * 
 * CONCEPTS TO LEARN:
 * 1. Custom Exceptions - Create specific exceptions for different error scenarios
 * 2. Extending RuntimeException - Unchecked exception (doesn't need try-catch)
 * 3. This allows us to throw meaningful exceptions in service layer
 */
public class ResourceNotFoundException extends RuntimeException {
    
    public ResourceNotFoundException(String message) {
        super(message);
    }
    
    public ResourceNotFoundException(String resourceName, Long id) {
        super(String.format("%s not found with id: %d", resourceName, id));
    }
}

