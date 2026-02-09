package com.security.spring.appmanager.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity(name = "AppManagerVersion")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "app_version", indexes = {
        @Index(name = "idx_app_version_app_name", columnList = "appName", unique = true)
})
public class AppVersion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String appName;

    @Column(nullable = false)
    private String version;

    /**
     * Relative path from upload-dir, e.g. "apps/ar7/ar7.apk"
     */
    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    private Instant updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }
}
