package com.security.spring.gamesoft.wager.service;

import com.security.spring.gamesoft.wager.entity.GameSoftWager;
import com.security.spring.gamesoft.wager.repository.GameSoftWagerReop;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GameSoftWagerServiceImpl implements GameSoftWagerService {

    @Autowired
    private GameSoftWagerReop gameSoftWaggerReop;

    @Override
    @Transactional
    public GameSoftWager saveWaggerObj(GameSoftWager gameSoftWagger) {
        GameSoftWager returnObj = gameSoftWaggerReop.save(gameSoftWagger);
        return returnObj;
    }



    @Override
    public Optional<GameSoftWager> findByWagerCode(String wagerCode) {
        return gameSoftWaggerReop.findByWagerID(wagerCode);
    }
}
