package com.security.spring.appmanager.service.impl;

import com.security.spring.appmanager.dto.AppVersionResponse;
import com.security.spring.appmanager.entity.AppVersion;
import com.security.spring.appmanager.repo.AppVersionRepository;
import com.security.spring.appmanager.service.AppManagerService;
import com.security.spring.exceptionall.DataNotFoundException;
import com.security.spring.storage.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;

@Service
@RequiredArgsConstructor
public class AppManagerServiceImpl implements AppManagerService {

    private final AppVersionRepository appVersionRepository;
    private final StorageService storageService;

    @Override
    public AppVersionResponse uploadApp(String appName, String version, MultipartFile file) throws IOException {
        if (appName == null || appName.isBlank()) {
            throw new IllegalArgumentException("App name is required");
        }
        if (version == null || version.isBlank()) {
            throw new IllegalArgumentException("Version is required");
        }
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("App file is required");
        }

        String fileName = storageService.uploadAppFile(file, appName);

        AppVersion appVersion = appVersionRepository.findByAppName(appName)
                .map(existing -> {
                    try {
                        storageService.deleteFileByRelativePath(existing.getFileName());
                    } catch (IOException e) {
                        // ignore if old file missing
                    }
                    existing.setVersion(version);
                    existing.setFileName(fileName);
                    return existing;
                })
                .orElse(AppVersion.builder()
                        .appName(appName.trim())
                        .version(version.trim())
                        .fileName(fileName)
                        .build());

        appVersion = appVersionRepository.save(appVersion);
        return toResponse(appVersion);
    }

    @Override
    public AppVersionResponse getVersionByAppName(String appName) {
        AppVersion app = appVersionRepository.findByAppName(appName)
                .orElseThrow(() -> new DataNotFoundException("App not found: " + appName));
        return toResponse(app);
    }

    @Override
    public ResponseEntity<Resource> downloadApp(String appName) throws IOException {
        AppVersion app = appVersionRepository.findByAppName(appName)
                .orElseThrow(() -> new DataNotFoundException("App not found: " + appName));

        var filePath = storageService.getAppFilePath(app.getFileName());
        if (!Files.exists(filePath) || !Files.isRegularFile(filePath)) {
            return ResponseEntity.notFound().build();
        }

        String displayFileName = filePath.getFileName().toString();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + displayFileName + "\"");
        headers.add(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate");

        Resource resource = new InputStreamResource(new FileInputStream(filePath.toFile()));
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(Files.size(filePath))
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    @Override
    public AppVersionResponse updateApp(String appName, String version, MultipartFile file) throws IOException {
        AppVersion app = appVersionRepository.findByAppName(appName)
                .orElseThrow(() -> new DataNotFoundException("App not found: " + appName));
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("App file is required for update");
        }

        storageService.updateAppFile(file, app.getFileName());
        if (version != null && !version.isBlank()) {
            app.setVersion(version.trim());
        }
        app = appVersionRepository.save(app);
        return toResponse(app);
    }

    private static AppVersionResponse toResponse(AppVersion app) {
        return AppVersionResponse.builder()
                .appName(app.getAppName())
                .version(app.getVersion())
                .createdAt(app.getCreatedAt())
                .updatedAt(app.getUpdatedAt())
                .build();
    }
}
