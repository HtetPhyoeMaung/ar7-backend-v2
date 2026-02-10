package com.security.spring.app.service.impl;

import com.security.spring.app.dto.AppVersionResponse;
import com.security.spring.app.entity.AppVersion;
import com.security.spring.app.repository.AppVersionRepository;
import com.security.spring.app.service.AppService;
import com.security.spring.storage.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AppServiceImpl implements AppService {

    private static final String DEFAULT_APP_KEY = "ar7";
    private static final String APK_EXT = ".apk";

    private final AppVersionRepository appVersionRepository;
    private final StorageService storageService;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Value("${file.cdn.domain}")
    private String cdnDomain;

    @Override
    public AppVersionResponse uploadApp(String appKey, String versionName, MultipartFile apkFile) {
        validateApkFile(apkFile);
        String effectiveKey = Optional.ofNullable(appKey).filter(s -> !s.isBlank()).orElse(DEFAULT_APP_KEY);
        if (appVersionRepository.findByAppKey(effectiveKey).isPresent()) {
            throw new IllegalArgumentException("App already exists for key: " + effectiveKey + ". Use update endpoint.");
        }
        String apkRelativePath;
        try {
            apkRelativePath = storageService.uploadAppFile(apkFile, effectiveKey);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to save APK file", e);
        }

        LocalDateTime now = LocalDateTime.now();
        AppVersion version = AppVersion.builder()
                .appKey(effectiveKey)
                .versionName(versionName != null ? versionName : "1.0.0")
                .versionCode(1)
                .apkFileName(apkRelativePath)
                .createdAt(now)
                .updatedAt(now)
                .build();
        appVersionRepository.save(version);
        return toResponse(version);
    }

    @Override
    public AppVersionResponse updateApp(String appKey, String versionName, MultipartFile apkFile) {
        validateApkFile(apkFile);
        String effectiveKey = Optional.ofNullable(appKey).filter(s -> !s.isBlank()).orElse(DEFAULT_APP_KEY);
        AppVersion existing = appVersionRepository.findByAppKey(effectiveKey)
                .orElseThrow(() -> new IllegalArgumentException("App not found for key: " + effectiveKey));

        String oldRelativePath = existing.getApkFileName();
        String newRelativePath;
        try {
            newRelativePath = storageService.uploadAppFile(apkFile, effectiveKey);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to save APK file", e);
        }
        if (!oldRelativePath.equals(newRelativePath)) {
            try {
                storageService.deleteFileByRelativePath(oldRelativePath);
            } catch (IOException e) {
                throw new IllegalStateException("Failed to delete old APK file", e);
            }
        }

        existing.setVersionName(versionName != null ? versionName : existing.getVersionName());
        existing.setApkFileName(newRelativePath);
        existing.setUpdatedAt(LocalDateTime.now());
        appVersionRepository.save(existing);
        return toResponse(existing);
    }

    @Override
    public AppVersionResponse checkVersion(String appKey) {
        String effectiveKey = Optional.ofNullable(appKey).filter(s -> !s.isBlank()).orElse(DEFAULT_APP_KEY);
        AppVersion version = appVersionRepository.findByAppKey(effectiveKey)
                .orElseThrow(() -> new IllegalArgumentException("App not found for key: " + effectiveKey));
        return toResponse(version);
    }

    @Override
    public List<AppVersionResponse> getApkList() {
        return appVersionRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public void deleteByAppKey(String appKey) {
        String effectiveKey = Optional.ofNullable(appKey).filter(s -> !s.isBlank()).orElse(DEFAULT_APP_KEY);
        AppVersion existing = appVersionRepository.findByAppKey(effectiveKey)
                .orElseThrow(() -> new IllegalArgumentException("App not found for key: " + effectiveKey));
        String apkRelativePath = existing.getApkFileName();
        try {
            storageService.deleteFileByRelativePath(apkRelativePath);
        } catch (IOException e) {
            // best effort: delete DB record even if file is missing or delete fails
        }
        appVersionRepository.delete(existing);
    }

    private void validateApkFile(MultipartFile apkFile) {
        if (apkFile == null || apkFile.isEmpty()) {
            throw new IllegalArgumentException("APK file is required");
        }
        String name = apkFile.getOriginalFilename();
        if (name == null || !name.toLowerCase().endsWith(APK_EXT)) {
            throw new IllegalArgumentException("File must be an APK");
        }
    }

    @Override
    public Resource getDownloadResource(String appKey) throws IOException {
        String effectiveKey = Optional.ofNullable(appKey).filter(s -> !s.isBlank()).orElse(DEFAULT_APP_KEY);
        AppVersion version = appVersionRepository.findByAppKey(effectiveKey)
                .orElseThrow(() -> new IllegalArgumentException("App not found for key: " + effectiveKey));
        Path filePath = storageService.getAppFilePath(version.getApkFileName());
        if (!java.nio.file.Files.exists(filePath) || !java.nio.file.Files.isRegularFile(filePath)) {
            throw new IOException("APK file not found: " + version.getApkFileName());
        }
        return new FileSystemResource(filePath.toFile());
    }

    private AppVersionResponse toResponse(AppVersion v) {
        String downloadUrl = cdnDomain + "/api/v1/app/download/" + v.getAppKey();
        return AppVersionResponse.builder()
                .appKey(v.getAppKey())
                .versionName(v.getVersionName())
                .versionCode(v.getVersionCode())
                .apkFileName(v.getApkFileName())
                .releaseNotes(v.getReleaseNotes())
                .updatedAt(v.getUpdatedAt())
                .downloadUrl(downloadUrl)
                .build();
    }
}
