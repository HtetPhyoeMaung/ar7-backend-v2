package com.security.spring.user.repository;

import com.security.spring.user.entity.RegisterStatus;
import com.security.spring.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegisterStatusRepo extends JpaRepository<RegisterStatus,Long> {
    RegisterStatus findByUser(User user);
}
