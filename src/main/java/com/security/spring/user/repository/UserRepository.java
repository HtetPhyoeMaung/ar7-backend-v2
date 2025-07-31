package com.security.spring.user.repository;

import com.security.spring.user.entity.User;
import com.security.spring.user.role.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByAr7Id(String ar7Id);
    Page<User> findByRole(Role role, Pageable pageable);
    Page<User> findByStatusIsFalse(Pageable pageable);
    Page<User> findByParentUserId(String parentId,Pageable pageable);
    List<User> findByParentUserId(String parentId);

    List<User> findByParentUserIdAndRoleAndStatus(String ar7Id, Role role, boolean b);

    List<User> findAllByAr7IdIn(Set<String> agentIds);

    boolean existsByAr7Id(String newAr7Id);

    Page<User> findByParentUserIdAndAr7IdContainingIgnoreCase(String ar7Id, String searchData, Pageable pageable);

    Page<User> findByStatusIsFalseAndAr7IdContainingIgnoreCase(String searchData, Pageable pageable);

    Page<User> findByRoleAndAr7IdContainingIgnoreCase(Role role, String searchData, Pageable pageable);
}
