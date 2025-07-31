package com.security.spring.spacetechmm.service;

import com.security.spring.dashboard.dto.DashboardResponse;
import com.security.spring.dashboard.dto.GameRequest;
import com.security.spring.spacetechmm.dto.SpaceTechGameDto;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;

public interface SpaceTechService {
    List<SpaceTechGameDto> getGameList(Long gameTypeId);

    ResponseEntity<DashboardResponse> createGame(GameRequest gameRequest) throws IOException;

    List<SpaceTechGameDto> getCardsGameByScreenId(Integer screenId, Long gameTypeId);

    ResponseEntity<DashboardResponse> updateGame(GameRequest gameRequest, long gameId) throws IOException;

    ResponseEntity<DashboardResponse> findById(int gameId);

    ResponseEntity<DashboardResponse> deleteById(int gameId);
}
