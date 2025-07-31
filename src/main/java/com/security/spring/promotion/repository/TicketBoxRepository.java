package com.security.spring.promotion.repository;

import com.security.spring.promotion.entity.TicketBox;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TicketBoxRepository extends JpaRepository<TicketBox, Long> {
    Optional<TicketBox> findByUser_Ar7Id(String userId);
}
