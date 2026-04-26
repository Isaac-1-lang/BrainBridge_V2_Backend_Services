package com.learn.brainbridge.controllers;

import com.learn.brainbridge.service.CloudinaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/images")
@Tag(name = "Image Upload", description = "Endpoints for managing profile pictures and course assets")
public class UploadImageController {

    @Autowired
    private CloudinaryService cloudinaryService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Upload an image file",
            description = "Accepts an image file (PNG, JPG) and uploads to Cloudinary, returns the public URL."
    )
    public ResponseEntity<?> uploadImage(
            @Parameter(
                    description = "The image file to upload",
                    content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            schema = @Schema(type = "string", format = "binary"))
            )
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "folder", required = false, defaultValue = "brainbridge") String folder) {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Please select a file to upload."));
        }

        // Validate file type
        String contentType = file.getContentType();
        if (contentType == null || (!contentType.startsWith("image/"))) {
            return ResponseEntity.badRequest().body(Map.of("error", "Only image files are allowed."));
        }

        // Validate file size (max 5MB)
        if (file.getSize() > 5 * 1024 * 1024) {
            return ResponseEntity.badRequest().body(Map.of("error", "File size must be less than 5MB."));
        }

        try {
            String imageUrl = cloudinaryService.uploadImage(file, folder);

            return ResponseEntity.ok(Map.of(
                    "url", imageUrl,
                    "name", file.getOriginalFilename(),
                    "size", file.getSize()
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Error uploading file: " + e.getMessage()));
        }
    }
}