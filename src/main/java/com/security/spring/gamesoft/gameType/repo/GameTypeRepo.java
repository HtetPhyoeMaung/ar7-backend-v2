package com.security.spring.gamesoft.gameType.repo;

import com.security.spring.gamesoft.gameType.entity.GameType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GameTypeRepo extends JpaRepository<GameType , Integer> {
    Optional<GameType> findByCode(String  code);

    Optional<GameType> findByDescription(String gameType);
}
