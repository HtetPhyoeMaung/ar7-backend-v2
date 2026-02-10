package com.security.spring.app.service;

import com.security.spring.app.dto.AppVersionResponse;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface AppService {

    AppVersionResponse uploadApp(String appKey, String versionName, MultipartFile apkFile);

    AppVersionResponse updateApp(String appKey, String versionName, MultipartFile apkFile);

    AppVersionResponse checkVersion(String appKey);

    List<AppVersionResponse> getApkList();

    void deleteByAppKey(String appKey);

    Resource getDownloadResource(String appKey) throws IOException;
}
