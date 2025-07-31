package com.security.spring.spacetechmm.service;

import com.security.spring.spacetechmm.dto.GameTableRequest;
import com.security.spring.spacetechmm.dto.GameTableResponseDto;

import java.io.IOException;

public interface GameTableService {

    GameTableResponseDto createGameTable(GameTableRequest request) throws IOException;

    GameTableResponseDto getAllGameTable(Long spaceTechGameId);

    GameTableResponseDto getGameTableId(Long id);

    GameTableResponseDto updateGameTable(Long id, GameTableRequest request) throws IOException;

    GameTableResponseDto deleteGameTable(Long id);

}