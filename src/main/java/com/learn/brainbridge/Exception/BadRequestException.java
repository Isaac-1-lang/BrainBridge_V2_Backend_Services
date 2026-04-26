package com.learn.brainbridge.Exception;

/**
 * BadRequestException - Custom exception for bad/invalid requests
 */
public class BadRequestException extends RuntimeException {
    
    public BadRequestException(String message) {
        super(message);
    }
}

