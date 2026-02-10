package com.security.spring.thirdpartygames.gameType.repo;

import com.security.spring.thirdpartygames.gameType.entity.GameType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GameTypeRepo extends JpaRepository<GameType , Integer> {
    Optional<GameType> findByCode(String  code);
    
    Optional<GameType> findByCodeIgnoreCase(String code);

    Optional<GameType> findByDescription(String gameType);
}
