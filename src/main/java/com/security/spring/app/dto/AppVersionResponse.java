package com.security.spring.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppVersionResponse {

    private String appKey;
    private String versionName;
    private Integer versionCode;
    private String apkFileName;
    private String releaseNotes;
    private LocalDateTime updatedAt;
    private String downloadUrl;
}
