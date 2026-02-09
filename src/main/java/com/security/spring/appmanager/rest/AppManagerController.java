package com.security.spring.appmanager.rest;

import com.security.spring.appmanager.dto.AppVersionResponse;
import com.security.spring.appmanager.service.AppManagerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/app-manager")
@RequiredArgsConstructor
@Tag(name = "App Manager", description = "Upload, get version, download, and update apps (e.g. APK)")
public class AppManagerController {

    private final AppManagerService appManagerService;

    @PostMapping(value = "/apps", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload app", description = "Upload an app with app name and version. Replaces existing app if same name.")
    public ResponseEntity<AppVersionResponse> uploadApp(
            @RequestParam("appName") String appName,
            @RequestParam("version") String version,
            @RequestParam("file") MultipartFile file) throws IOException {
        AppVersionResponse response = appManagerService.uploadApp(appName, version, file);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/apps/{appName}/version")
    @Operation(summary = "Get version by app name", description = "Returns current version info for the given app name.")
    public ResponseEntity<AppVersionResponse> getVersion(@PathVariable String appName) {
        AppVersionResponse response = appManagerService.getVersionByAppName(appName);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/apps/{appName}/download")
    @Operation(summary = "Download app", description = "Download the app file by app name.")
    public ResponseEntity<org.springframework.core.io.Resource> downloadApp(@PathVariable String appName) throws IOException {
        return appManagerService.downloadApp(appName);
    }

    @PutMapping(value = "/apps/{appName}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Update app", description = "Update the app file and optionally version for the given app name.")
    public ResponseEntity<AppVersionResponse> updateApp(
            @PathVariable String appName,
            @RequestParam(value = "version", required = false) String version,
            @RequestParam("file") MultipartFile file) throws IOException {
        AppVersionResponse response = appManagerService.updateApp(appName, version, file);
        return ResponseEntity.ok(response);
    }
}
