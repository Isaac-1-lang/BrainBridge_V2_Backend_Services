package com.learn.brainbridge.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

/**
 * CorsConfig - Configuration for Cross-Origin Resource Sharing (CORS)
 * 
 * CONCEPTS TO LEARN:
 * 1. CORS - Allows frontend (running on different port) to access backend API
 * 2. @Configuration - Marks this as a Spring configuration class
 * 3. CorsFilter - Spring filter that handles CORS preflight requests
 * 
 * This allows your NextJS frontend (typically on port 3000 or another other one) to make requests
 * to your Spring Boot backend (on port 8080 or Any other one)
 */
@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        
        // Allow credentials (cookies, authorization headers)
        config.setAllowCredentials(true);
        
        // Allow requests from frontend origin (Vite default port is 5173)
        config.setAllowedOriginPatterns(Arrays.asList(
            "http://localhost:*",// Vite dev server
            "https://*.vercel.app",
            "http://127.0.0.1:*"
        ));
        
        // Allow all HTTP methods
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        
        // Allow all headers
        config.setAllowedHeaders(Arrays.asList("*"));
        
        // Expose headers that frontend might need
        config.setExposedHeaders(Arrays.asList(
            "Authorization",
            "Content-Type",
            "X-Total-Count"
        ));
        
        // Cache preflight requests for 1 hour
        config.setMaxAge(3600L);
        
        // Apply CORS configuration to all paths
        source.registerCorsConfiguration("/**", config);
        
        return new CorsFilter(source);
    }
}

