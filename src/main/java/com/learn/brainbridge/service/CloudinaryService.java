package com.learn.brainbridge.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {

    private static final Logger log = LoggerFactory.getLogger(CloudinaryService.class);

    @Autowired
    private Cloudinary cloudinary;

    public String uploadImage(MultipartFile file, String folder) throws IOException {
        // Log the config being used (mask the secret)
        Map<String, Object> config = cloudinary.config.asMap();
        String cloudName = String.valueOf(config.get("cloud_name"));
        String apiKey    = String.valueOf(config.get("api_key"));
        String apiSecret = String.valueOf(config.get("api_secret"));

        log.info("=== Cloudinary Upload Attempt ===");
        log.info("  cloud_name : '{}'", cloudName);
        log.info("  api_key    : '{}'", apiKey);
        log.info("  api_secret : '{}'", apiSecret.length() > 4
                ? apiSecret.substring(0, 4) + "****" : "(empty/missing)");
        log.info("  folder     : '{}'", folder);
        log.info("  file name  : '{}'", file.getOriginalFilename());
        log.info("  file size  : {} bytes", file.getSize());
        log.info("  content-type: '{}'", file.getContentType());

        // Detect placeholder values and fail fast with a clear message
        if ("your-cloud-name".equals(cloudName) || cloudName.isBlank()
                || "your-api-key".equals(apiKey) || apiKey.isBlank()
                || "your-api-secret".equals(apiSecret) || apiSecret.isBlank()) {
            log.error("Cloudinary credentials are still placeholder values! " +
                      "Set CLOUDINARY_CLOUD_NAME, CLOUDINARY_API_KEY, CLOUDINARY_API_SECRET " +
                      "in your environment or application.properties.");
            throw new IllegalStateException(
                "Cloudinary is not configured. Please set your cloud_name, api_key, and api_secret.");
        }

        Map<String, Object> uploadParams = ObjectUtils.asMap(
                "folder", folder != null ? folder : "brainbridge",
                "resource_type", "image"
        );

        try {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), uploadParams);
            String url = (String) uploadResult.get("secure_url");
            log.info("Upload successful. URL: {}", url);
            return url;
        } catch (Exception e) {
            log.error("Cloudinary upload failed: {}", e.getMessage(), e);
            throw e;
        }
    }

    public void deleteImage(String publicId) throws IOException {
        log.info("Deleting Cloudinary image with publicId: {}", publicId);
        cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
    }
}
