package com.security.spring.app.repository;

import com.security.spring.app.entity.AppVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("appVersionRepository")
public interface AppVersionRepository extends JpaRepository<AppVersion, Long> {

    Optional<AppVersion> findByAppKey(String appKey);
}
