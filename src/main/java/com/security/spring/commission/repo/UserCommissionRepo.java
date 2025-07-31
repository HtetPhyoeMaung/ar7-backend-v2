package com.security.spring.commission.repo;

import com.security.spring.commission.entity.UserCommission;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

@ReadingConverter
public interface UserCommissionRepo extends JpaRepository<UserCommission,Integer> {
    UserCommission findByUser_ar7IdAndGameType_code(String ar7Id, String  gameCode);

    List<UserCommission> findByUser_ar7Id(String ar7Id);

    Page<UserCommission> findByUpLineAr7Id(String ar7Id, Pageable pageable);

    List<UserCommission> findByUserAr7IdIn(Set<String> agentIds);
}
