package com.security.spring;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class FileDownloadController {

    @Value("${file.upload-dir}")
    private String uploadDir;

    /**
     * Download APK by relative path (e.g. ar7.apk or apps/ar7/ar7.apk).
     * Example: GET /download/apk/apps/ar7/ar7.apk
     */
    @GetMapping("/download/apk/{apkPath:.+}")
    public ResponseEntity<InputStreamResource> downloadApk(@PathVariable String apkPath) {
        try {
            if (apkPath == null || apkPath.contains("..")) {
                return ResponseEntity.badRequest().build();
            }
            Path basePath = Paths.get(uploadDir).normalize();
            Path filePath = basePath.resolve(apkPath).normalize();
            if (!filePath.startsWith(basePath)) {
                return ResponseEntity.badRequest().build();
            }
            File file = filePath.toFile();
            if (!file.exists() || !file.isFile()) {
                return ResponseEntity.notFound().build();
            }
            String filename = file.getName();
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");
            headers.add(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate");

            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(file.length())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}