package com.security.spring.spacetechmm.repo;

import com.security.spring.spacetechmm.entity.GameTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GameTableRepository  extends JpaRepository<GameTable , Long> {


    List<GameTable> findAllBySpaceTechGame_Id(Long spaceTechGameId);


    Optional<GameTable> findByLevelAndSpaceTechGame_Id(int i, Long spaceTechId);
}
