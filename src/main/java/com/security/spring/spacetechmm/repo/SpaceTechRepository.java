package com.security.spring.spacetechmm.repo;

import com.security.spring.spacetechmm.entity.SpaceTechGame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
public interface SpaceTechRepository extends JpaRepository<SpaceTechGame,Long> {
    boolean existsByGameNameAndGameType_Id(String gameName, int gameType);


    List<SpaceTechGame> findAllByGameType_Id(Long gameTypeId);

    Optional<SpaceTechGame> findByGameName(String game);

    List<SpaceTechGame> findByGameNameContainingIgnoreCase(String game);
}
