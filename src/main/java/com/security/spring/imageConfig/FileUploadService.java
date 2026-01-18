package com.security.spring.imageConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileUploadService {
    private final Logger logger = LoggerFactory.getLogger(FileUploadService.class);

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Value("${file.cdn.domain}")
    private String cdnDomain;

    public String uploadFile(MultipartFile file, String name, String folderName) {
        try {
            // Ensure upload directory exists
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Create folder if needed
            Path folderPath = uploadPath.resolve(folderName);
            if (!Files.exists(folderPath)) {
                Files.createDirectories(folderPath);
            }

            // Generate unique file name
            String fileName = UUID.randomUUID().toString() + "_" + name;
            Path filePath = folderPath.resolve(fileName);

            // Save file to local directory
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Return CDN URL
            String relativePath = folderName + "/" + fileName;
            String fileUrl = cdnDomain + "/api/v1/files/" + relativePath;
            logger.info("File uploaded successfully: {}", fileUrl);
            return fileUrl;

        } catch (IOException e) {
            logger.error("Failed to upload file: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to upload file", e);
        }
    }

    public boolean deleteFile(String fileUrl) {
        try {
            if (fileUrl == null || fileUrl.isEmpty()) {
                logger.warn("File URL is null or empty");
                return false;
            }

            // Extract relative path from URL
            String relativePath = extractPathFromUrl(fileUrl);
            
            // Delete file from local directory
            Path filePath = Paths.get(uploadDir, relativePath);
            if (Files.exists(filePath)) {
                Files.delete(filePath);
                logger.info("Successfully deleted file: {}", relativePath);
                return true;
            } else {
                logger.warn("File not found: {}", relativePath);
                return false;
            }

        } catch (Exception e) {
            logger.error("Error deleting file: {}", e.getMessage(), e);
            return false;
        }
    }

    private String extractPathFromUrl(String fileUrl) {
        // Extract relative path from CDN URL
        // URL format: https://domain.com/api/v1/files/folder/filename
        String prefix = cdnDomain + "/api/v1/files/";
        if (fileUrl.startsWith(prefix)) {
            return fileUrl.substring(prefix.length());
        }
        // Fallback: if URL contains /api/v1/files/, extract after it
        int index = fileUrl.indexOf("/api/v1/files/");
        if (index != -1) {
            return fileUrl.substring(index + "/api/v1/files/".length());
        }
        // If no pattern matches, try to extract filename
        return fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
    }
}