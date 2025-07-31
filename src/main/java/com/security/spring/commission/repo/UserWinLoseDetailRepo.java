package com.security.spring.commission.repo;

import com.security.spring.commission.entity.UserWinLoseDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserWinLoseDetailRepo extends JpaRepository<UserWinLoseDetail,Integer> {
   


    List<UserWinLoseDetail> findByParentAgentId(String agentAr7Id);

    Page<UserWinLoseDetail> findByAgentCommissionStatusAndConfirmIsFalse(boolean b, Pageable pageable);
}
