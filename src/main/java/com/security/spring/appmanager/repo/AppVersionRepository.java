package com.security.spring.appmanager.repo;

import com.security.spring.appmanager.entity.AppVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("appManagerAppVersionRepository")
public interface AppVersionRepository extends JpaRepository<AppVersion, Long> {

    Optional<AppVersion> findByAppName(String appName);

    boolean existsByAppName(String appName);
}
