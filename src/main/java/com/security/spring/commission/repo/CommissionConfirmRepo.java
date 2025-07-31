package com.security.spring.commission.repo;

import com.security.spring.commission.entity.CommissionConfirm;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface CommissionConfirmRepo extends JpaRepository<CommissionConfirm,Long> {

    List<CommissionConfirm> findByConfirmDateBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
}
