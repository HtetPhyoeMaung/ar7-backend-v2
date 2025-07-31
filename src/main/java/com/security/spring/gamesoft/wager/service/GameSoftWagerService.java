package com.security.spring.gamesoft.wager.service;

import com.security.spring.gamesoft.wager.entity.GameSoftWager;

import java.util.Optional;

public interface GameSoftWagerService {
     GameSoftWager saveWaggerObj(GameSoftWager gameSoftWagger);



    Optional<GameSoftWager> findByWagerCode(String wagerCode);
}
