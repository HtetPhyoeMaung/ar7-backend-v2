package com.security.spring.buffalo.repo;

import com.security.spring.buffalo.model.BuffaloSetting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BuffaloSettingRepo extends JpaRepository<BuffaloSetting, Long> {
}
