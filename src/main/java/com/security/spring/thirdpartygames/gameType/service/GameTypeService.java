package com.security.spring.thirdpartygames.gameType.service;

import com.security.spring.thirdpartygames.gameType.dto.GameTypeRequest;
import com.security.spring.thirdpartygames.gameType.dto.GameTypeResponse;
import com.security.spring.thirdpartygames.gameType.entity.GameType;

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
