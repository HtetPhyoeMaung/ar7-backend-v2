package com.security.spring.unit.repo;

import com.security.spring.unit.entity.TransitionHistory;
import com.security.spring.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransitionHistoryRepo extends JpaRepository<TransitionHistory,Integer> {

    Page<TransitionHistory> findByActionTimeBetweenAndFromUserOrToUser(LocalDateTime startDate, LocalDateTime endDate, User currentUserGet, User currentUserGet1, Pageable pageable);

    Page<TransitionHistory> findByActionTimeBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    Page<TransitionHistory> findByFromUserOrToUser(User currentUser, User currentUser1, Pageable pageable);
}
