package com.security.spring.imageConfig;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class FileUploadService {
    private final Logger logger = LoggerFactory.getLogger(FileUploadService.class);

    @Autowired
    private AmazonS3 s3Client;

    @Value("${digitalocean.spaces.bucket-name}")
    private String bucketName;

    public String uploadFile(MultipartFile file, String name, String folderName) {
        try {
            String folderPath = folderName;
            String fileName = folderPath + UUID.randomUUID().toString() + "_" + name;

            // Upload file to Digital Ocean Space
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());

            s3Client.putObject(bucketName, fileName, file.getInputStream(), metadata);

            // Get file URL
            String fileUrl = s3Client.getUrl(bucketName, fileName).toString();
            return fileUrl;

        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file", e);
        }
    }

    public boolean deleteFile(String fileUrl) {
        try {
            if (fileUrl == null || fileUrl.isEmpty()) {
                logger.warn("File URL is null or empty");
                return false;
            }

            // Extract key (file path) from URL
            String key = extractKeyFromUrl(fileUrl);

            // Delete object from Digital Ocean Space
            s3Client.deleteObject(new DeleteObjectRequest(bucketName, key));

            logger.info("Successfully deleted file: {}", key);
            return true;

        } catch (Exception e) {
            logger.error("Error deleting file: {}", e.getMessage());
            return false;
        }
    }

    private String extractKeyFromUrl(String fileUrl) {
        // Remove the bucket URL part to get the key
        String bucketUrl = s3Client.getUrl(bucketName, "").toString();
        return fileUrl.replace(bucketUrl, "");
    }
}