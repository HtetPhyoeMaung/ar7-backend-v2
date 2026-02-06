package com.security.spring.storage;

import com.security.spring.utils.UUIDGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import javax.imageio.ImageIO;

@Service
public class StorageService {
    
    @Value("${file.upload-dir}")
    private String uploadDir;

    @Value("${file.cdn.domain}")
    private String cdnDomain;
    public BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        Image resultingImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
        BufferedImage outputImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(resultingImage, 0, 0, null);
        g2d.dispose();
        return outputImage;
    }
    
    private Path ensureUploadDirectory() throws IOException {
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        return uploadPath;
    }
    public String uploadImage(MultipartFile file) throws IOException {
        // Ensure upload directory exists
        Path uploadPath = ensureUploadDirectory();
        
        // Generate unique file name
        String filename = UUIDGenerator.generateUUID() + file.getOriginalFilename();
        
        // Convert MultipartFile to BufferedImage
        BufferedImage inputImage = ImageIO.read(file.getInputStream());
        
        // Resize image (you can specify target width and height here)
        int targetWidth = 500;
        int targetHeight = 300;
        BufferedImage resizedImage = resizeImage(inputImage, targetWidth, targetHeight);
        
        // Convert resized BufferedImage to file
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(resizedImage, "jpg", baos);
        byte[] resizedBytes = baos.toByteArray();
        
        // Save resized image to local directory
        Path filePath = uploadPath.resolve(filename);
        Files.write(filePath, resizedBytes);
        
        return filename;
    }

    public String uploadFile(byte[] fileData, String fileName, String contentType) throws IOException {
        // Ensure upload directory exists
        Path uploadPath = ensureUploadDirectory();
        
        // Save file to local directory
        Path filePath = uploadPath.resolve(fileName);
        Files.write(filePath, fileData);
        
        // Return CDN URL
        return cdnDomain + "/api/v1/files/" + fileName;
    }


    public String getImageByName(String imageName) {
        // Return CDN URL for the image
        return cdnDomain + "/api/v1/files/" + imageName;
    }

    public String updateImage(MultipartFile file, String filename) throws IOException {
        // Ensure upload directory exists
        Path uploadPath = ensureUploadDirectory();
        
        // Check if the file exists and delete it
        Path existingFilePath = uploadPath.resolve(filename);
        if (Files.exists(existingFilePath)) {
            Files.delete(existingFilePath);
        }
        
        // Generate new unique file name
        String imageName = UUIDGenerator.generateUUID() + file.getOriginalFilename();
        
        // Save the new file to local directory
        Path filePath = uploadPath.resolve(imageName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        
        return imageName;
    }

    public void deleteImage(String imageName) {
        try {
            // Delete file from local directory
            Path filePath = Paths.get(uploadDir, imageName);
            if (Files.exists(filePath)) {
                Files.delete(filePath);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete image: " + imageName, e);
        }
    }

    /**
     * Upload app file (e.g. APK) under apps/{appName}/. Returns relative path from uploadDir.
     */
    public String uploadAppFile(MultipartFile file, String appName) throws IOException {
        Path uploadPath = ensureUploadDirectory();
        String safeAppName = appName.replaceAll("[^a-zA-Z0-9_-]", "_");
        Path appDir = uploadPath.resolve("apps").resolve(safeAppName);
        if (!Files.exists(appDir)) {
            Files.createDirectories(appDir);
        }
        String originalFilename = file.getOriginalFilename();
        String fileName = (originalFilename != null && !originalFilename.isBlank())
                ? originalFilename
                : "app.apk";
        Path filePath = appDir.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        return "apps/" + safeAppName + "/" + fileName;
    }

    /**
     * Replace existing app file. relativePath is e.g. "apps/ar7/ar7.apk".
     */
    public String updateAppFile(MultipartFile file, String relativePath) throws IOException {
        Path fullPath = Paths.get(uploadDir, relativePath).normalize();
        Path uploadPath = Paths.get(uploadDir).normalize();
        if (!fullPath.startsWith(uploadPath) || !Files.exists(fullPath)) {
            throw new IOException("Invalid or missing app file path: " + relativePath);
        }
        Files.copy(file.getInputStream(), fullPath, StandardCopyOption.REPLACE_EXISTING);
        return relativePath;
    }

    public Path getAppFilePath(String relativePath) {
        return Paths.get(uploadDir, relativePath).normalize();
    }

    public void deleteFileByRelativePath(String relativePath) throws IOException {
        Path filePath = Paths.get(uploadDir, relativePath).normalize();
        Path uploadPath = Paths.get(uploadDir).normalize();
        if (!filePath.startsWith(uploadPath)) {
            throw new IOException("Invalid path: " + relativePath);
        }
        if (Files.exists(filePath)) {
            Files.delete(filePath);
        }
    }
}