package com.security.spring.app.service.impl;

import com.security.spring.app.dto.AppVersionResponse;
import com.security.spring.app.entity.AppVersion;
import com.security.spring.app.repository.AppVersionRepository;
import com.security.spring.app.service.AppService;
import com.security.spring.storage.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.time.LocalDateTime;
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

    @Override
    public AppVersionResponse uploadApp(String appKey, String versionName, Integer versionCode,
                                        String releaseNotes, MultipartFile apkFile) {
        validateApkFile(apkFile);
        String effectiveKey = Optional.ofNullable(appKey).filter(s -> !s.isBlank()).orElse(DEFAULT_APP_KEY);
        if (appVersionRepository.findByAppKey(effectiveKey).isPresent()) {
            throw new IllegalArgumentException("App already exists for key: " + effectiveKey + ". Use update endpoint.");
        }
        String apkFileName = resolveApkFileName(apkFile, effectiveKey);
        try {
            storageService.saveApk(apkFile, apkFileName);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to save APK file", e);
        }

        LocalDateTime now = LocalDateTime.now();
        AppVersion version = AppVersion.builder()
                .appKey(effectiveKey)
                .versionName(versionName != null ? versionName : "1.0.0")
                .versionCode(versionCode != null ? versionCode : 1)
                .apkFileName(apkFileName)
                .releaseNotes(releaseNotes)
                .createdAt(now)
                .updatedAt(now)
                .build();
        appVersionRepository.save(version);
        return toResponse(version);
    }

    @Override
    public AppVersionResponse updateApp(String appKey, String versionName, Integer versionCode,
                                        String releaseNotes, MultipartFile apkFile) {
        validateApkFile(apkFile);
        String effectiveKey = Optional.ofNullable(appKey).filter(s -> !s.isBlank()).orElse(DEFAULT_APP_KEY);
        AppVersion existing = appVersionRepository.findByAppKey(effectiveKey)
                .orElseThrow(() -> new IllegalArgumentException("App not found for key: " + effectiveKey));

        String oldFileName = existing.getApkFileName();
        String newApkFileName = resolveApkFileName(apkFile, effectiveKey);
        try {
            storageService.saveApk(apkFile, newApkFileName);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to save APK file", e);
        }
        if (!oldFileName.equals(newApkFileName)) {
            storageService.deleteFile(oldFileName);
        }

        existing.setVersionName(versionName != null ? versionName : existing.getVersionName());
        existing.setVersionCode(versionCode != null ? versionCode : existing.getVersionCode());
        existing.setApkFileName(newApkFileName);
        existing.setReleaseNotes(releaseNotes != null ? releaseNotes : existing.getReleaseNotes());
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

    private void validateApkFile(MultipartFile apkFile) {
        if (apkFile == null || apkFile.isEmpty()) {
            throw new IllegalArgumentException("APK file is required");
        }
        String name = apkFile.getOriginalFilename();
        if (name == null || !name.toLowerCase().endsWith(APK_EXT)) {
            throw new IllegalArgumentException("File must be an APK");
        }
    }

    private String resolveApkFileName(MultipartFile apkFile, String appKey) {
        String original = apkFile.getOriginalFilename();
        if (original != null && original.toLowerCase().endsWith(APK_EXT)) {
            return original;
        }
        return appKey + APK_EXT;
    }

    private AppVersionResponse toResponse(AppVersion v) {
        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        String downloadUrl = baseUrl + "/download/apk/" + v.getApkFileName();
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
