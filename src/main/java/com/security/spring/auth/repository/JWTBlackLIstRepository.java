package com.security.spring.auth.repository;

import com.security.spring.auth.entity.JWTBlackList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JWTBlackLIstRepository extends JpaRepository<JWTBlackList, Long> {
    boolean existsByToken(String token);
}
