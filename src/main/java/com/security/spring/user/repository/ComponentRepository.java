package com.security.spring.user.repository;

import com.security.spring.user.role.Component;
import com.security.spring.user.role.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComponentRepository extends JpaRepository<Component, Long> {
    Component findByRole(Role role);
}
