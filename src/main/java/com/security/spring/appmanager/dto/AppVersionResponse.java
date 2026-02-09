package com.security.spring.appmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppVersionResponse {

    private String appName;
    private String version;
    private Instant createdAt;
    private Instant updatedAt;
}
