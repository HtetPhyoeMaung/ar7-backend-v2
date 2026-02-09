package com.security.spring.app.rest;

import com.security.spring.app.dto.AppVersionResponse;
import com.security.spring.app.service.AppService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/app")
@RequiredArgsConstructor
@Tag(name = "App", description = "App upload, update and version check")
public class AppController {

    private final AppService appService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload app APK", description = "Upload a new app APK and register version info")
    public ResponseEntity<AppVersionResponse> uploadApp(
            @RequestParam(value = "appKey", required = false) String appKey,
            @RequestParam(value = "versionName", required = false) String versionName,
            @RequestParam(value = "versionCode", required = false) Integer versionCode,
            @RequestParam(value = "releaseNotes", required = false) String releaseNotes,
            @RequestParam("apk") MultipartFile apkFile) {
        AppVersionResponse response = appService.uploadApp(appKey, versionName, versionCode, releaseNotes, apkFile);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Update app APK", description = "Update existing app with new APK and version info")
    public ResponseEntity<AppVersionResponse> updateApp(
            @RequestParam(value = "appKey", required = false) String appKey,
            @RequestParam(value = "versionName", required = false) String versionName,
            @RequestParam(value = "versionCode", required = false) Integer versionCode,
            @RequestParam(value = "releaseNotes", required = false) String releaseNotes,
            @RequestParam("apk") MultipartFile apkFile) {
        AppVersionResponse response = appService.updateApp(appKey, versionName, versionCode, releaseNotes, apkFile);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/version")
    @Operation(summary = "Check app version", description = "Get current app version info for the given app key")
    public ResponseEntity<AppVersionResponse> checkVersion(
            @RequestParam(value = "appKey", required = false) String appKey) {
        AppVersionResponse response = appService.checkVersion(appKey);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/list")
    @Operation(summary = "Get APK list", description = "Get list of all registered APKs with download links")
    public ResponseEntity<List<AppVersionResponse>> getApkList() {
        return ResponseEntity.ok(appService.getApkList());
    }
}
