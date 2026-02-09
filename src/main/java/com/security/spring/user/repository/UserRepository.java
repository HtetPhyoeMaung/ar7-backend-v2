package com.security.spring.user.repository;

import com.security.spring.user.entity.User;
import com.security.spring.user.role.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RestResource; // Required import
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByAr7Id(String ar7Id);
    Optional<User> findByName(String name);
    Page<User> findByRole(Role role, Pageable pageable);
    Page<User> findByStatusIsFalse(Pageable pageable);
    
    // Keep this one for the REST API
    Page<User> findByParentUserId(String parentId, Pageable pageable);
    
    // Hide this one from REST API to prevent the collision crash
    @RestResource(exported = false)
    List<User> findByParentUserId(String parentId);

    List<User> findByParentUserIdAndRoleAndStatus(String ar7Id, Role role, boolean b);

    List<User> findAllByAr7IdIn(Set<String> agentIds);

    boolean existsByAr7Id(String newAr7Id);

    Page<User> findByParentUserIdAndAr7IdContainingIgnoreCase(String ar7Id, String searchData, Pageable pageable);

    Page<User> findByParentUserIdAndRole(String parentUserId, Role role, Pageable pageable);

    Page<User> findByParentUserIdAndRoleAndAr7IdContainingIgnoreCase(String parentUserId, Role role, String searchData, Pageable pageable);

    Page<User> findByStatusIsFalseAndAr7IdContainingIgnoreCase(String searchData, Pageable pageable);

    Page<User> findByRoleAndAr7IdContainingIgnoreCase(Role role, String searchData, Pageable pageable);

    // Keep this one for the REST API
    Page<User> findByPromoCode(String code, Pageable pageable);

    // Hide this one from REST API to prevent the collision crash
    @RestResource(exported = false)
    Optional<User> findByPromoCode(String code);

    // Note: I left the 'AndAnd' as is per your request, 
    // but if the app fails to start, remove one 'And'.
    Page<User> findByPromoCodeAndAndAr7IdContainingIgnoreCase(String code, String searchData, Pageable pageable);

    Optional<User> findByCode(String code);

    boolean existsByCode(String code);
}