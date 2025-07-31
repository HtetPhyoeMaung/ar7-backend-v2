package com.security.spring.gamesoft.gameType.service;

import com.security.spring.gamesoft.gameType.dto.GameTypeRequest;
import com.security.spring.gamesoft.gameType.dto.GameTypeResponse;
import com.security.spring.gamesoft.gameType.entity.GameType;

public interface GameTypeService {
     GameTypeResponse saveGameType(GameTypeRequest gameType);
     GameTypeResponse getAllGameType();
     GameTypeResponse updateGameType(GameTypeRequest data);
     GameTypeResponse getGameTypeResponseById(int id);
     GameType findByCode(String  gameType);

    GameTypeResponse deleteGameTypeById(int id);

    GameTypeResponse getAllSpaceTechGameTypes();

    GameType findById(int gameType);
}
