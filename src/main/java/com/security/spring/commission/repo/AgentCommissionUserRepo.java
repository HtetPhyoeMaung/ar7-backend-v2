package com.security.spring.commission.repo;

import com.security.spring.commission.entity.AgentCommissionUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgentCommissionUserRepo extends JpaRepository<AgentCommissionUser,Integer> {
}
