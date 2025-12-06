package com.security.spring.thirdpartygames.wager.service;

import com.security.spring.thirdpartygames.wager.entity.GameSoftWager;

import java.util.Optional;

public interface GameSoftWagerService {
     GameSoftWager saveWaggerObj(GameSoftWager gameSoftWagger);



    Optional<GameSoftWager> findByWagerCode(String wagerCode);
}
