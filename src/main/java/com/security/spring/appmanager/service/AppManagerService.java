package com.security.spring.appmanager.service;

import com.security.spring.appmanager.dto.AppVersionResponse;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface AppManagerService {

    AppVersionResponse uploadApp(String appName, String version, MultipartFile file) throws IOException;

    AppVersionResponse getVersionByAppName(String appName);

    ResponseEntity<Resource> downloadApp(String appName) throws IOException;

    AppVersionResponse updateApp(String appName, String version, MultipartFile file) throws IOException;
}
