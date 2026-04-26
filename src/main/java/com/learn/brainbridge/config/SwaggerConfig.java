package com.learn.brainbridge.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * SwaggerConfig - Configuration for API Documentation
 * 
 * CONCEPTS TO LEARN:
 * 1. @Configuration - Marks this class as a Spring configuration class
 * 2. @Bean - Methods annotated with @Bean create Spring-managed objects
 * 3. OpenAPI - Standard for API documentation (formerly Swagger)
 * 4. Swagger UI - Interactive API documentation interface
 * 
 * Access Swagger UI at: http://localhost:8080/swagger-ui.html
 * Access OpenAPI JSON at: http://localhost:8080/v3/api-docs
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI brainBridgeOpenAPI() {
        Server devServer = new Server();
        devServer.setUrl("http://localhost:8081");
        devServer.setDescription("Development Server");

        Server prodServer = new Server();
        prodServer.setUrl("https://brain-bridge-eight.vercel.app/");
        prodServer.setDescription("Production Server");

        Contact contact = new Contact();
        contact.setEmail("isaprecieux112@gmail.com");
        contact.setName("BrainBridge Support");
        contact.setUrl("https://brain-bridge-eight.vercel.app/");

        License license = new License()
                .name("MIT License")
                .url("https://opensource.org/licenses/MIT");

        Info info = new Info()
                .title("BrainBridge API")
                .version("1.0.0")
                .contact(contact)
                .description("RESTful API for BrainBridge - A collaborative coding platform")
                .termsOfService("https://brainbridge.com/terms")
                .license(license);

        return new OpenAPI()
                .info(info)
                .servers(List.of(devServer, prodServer));
    }

    /**
     * Configure which packages Swagger should scan
     * This helps avoid scanning entity classes and focuses on DTOs
     * Using default group name to avoid issues
     */
    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("default")
                .pathsToMatch("/api/**", "/projects/**", "/messages/**", "/notifications/**", "/favorites/**")
                .build();
    }
}

