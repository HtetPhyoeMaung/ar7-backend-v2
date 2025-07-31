package com.security.spring.unit.repo;

import com.security.spring.unit.entity.UserUnits;
import com.security.spring.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UnitRepository extends JpaRepository<UserUnits,Long> {
    Optional<UserUnits> findByUser(User user);

    Optional<UserUnits> findByUser_Ar7Id(String agentAr7Id);
}
