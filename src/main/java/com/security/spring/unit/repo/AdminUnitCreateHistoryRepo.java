package com.security.spring.unit.repo;

import com.security.spring.unit.entity.AdminUnitCreateHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface AdminUnitCreateHistoryRepo extends JpaRepository<AdminUnitCreateHistory,Long> {
    Page<AdminUnitCreateHistory> findByDateTimeBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
}
