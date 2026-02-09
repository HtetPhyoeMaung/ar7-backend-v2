package com.security.spring.app.service;

import com.security.spring.app.dto.AppVersionResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AppService {

    AppVersionResponse uploadApp(String appKey, String versionName, Integer versionCode,
                                 String releaseNotes, MultipartFile apkFile);

    AppVersionResponse updateApp(String appKey, String versionName, Integer versionCode,
                                 String releaseNotes, MultipartFile apkFile);

    AppVersionResponse checkVersion(String appKey);

    List<AppVersionResponse> getApkList();
}
