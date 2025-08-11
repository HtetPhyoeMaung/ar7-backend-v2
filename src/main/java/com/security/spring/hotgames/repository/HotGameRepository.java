package com.security.spring.hotgames.repository;

import com.security.spring.hotgames.entity.HotGame;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HotGameRepository extends JpaRepository<HotGame, Integer> {
}
