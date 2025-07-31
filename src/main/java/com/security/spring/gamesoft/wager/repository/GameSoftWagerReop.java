package com.security.spring.gamesoft.wager.repository;

import com.security.spring.gamesoft.wager.entity.GameSoftWager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GameSoftWagerReop extends JpaRepository<GameSoftWager, Integer> {
//    Optional<GameSoftWager> findByWagerId(Long wagerID);

    Optional<GameSoftWager> findByWagerID(String wagerId);
}
