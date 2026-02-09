package com.security.spring.app.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity(name = "AppVersionByKey")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "app_version", indexes = {
        @Index(name = "idx_app_version_key", columnList = "appKey")
})
public class AppVersion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String appKey;

    @Column(nullable = false)
    private String versionName;

    @Column(nullable = false)
    private Integer versionCode;

    @Column(nullable = false)
    private String apkFileName;

    private String releaseNotes;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
