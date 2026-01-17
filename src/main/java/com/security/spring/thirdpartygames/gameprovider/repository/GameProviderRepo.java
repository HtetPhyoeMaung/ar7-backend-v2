package com.security.spring.thirdpartygames.gameprovider.repository;

import com.security.spring.thirdpartygames.gameType.entity.GameType;
import com.security.spring.thirdpartygames.gameprovider.entity.GameSoftGameProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GameProviderRepo extends JpaRepository<GameSoftGameProvider,Integer> {
    Optional<GameSoftGameProvider> findByProductAndGameType(Long product, GameType gameType);
    List<GameSoftGameProvider> findByGameType(GameType gameType);

    void deleteAllByProductCodeIn(List<String> rejectProviderList);

    Optional<GameSoftGameProvider> findByProduct(Long product);
}
