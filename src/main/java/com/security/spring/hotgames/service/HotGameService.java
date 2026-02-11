package com.security.spring.hotgames.service;

import java.util.List;
import java.util.Map;

import com.security.spring.hotgames.dto.AddHotGameRequest;
import com.security.spring.hotgames.entity.HotGameItem;
import com.security.spring.thirdpartygames.getGameList.dto.ProviderGame;

public interface HotGameService {
    Map<String, List<ProviderGame>> getHotGames();
    List<HotGameItem> getHotGameItems();
    void refreshHotGames();
    void addHotGame(AddHotGameRequest request);
    void updateHotGame(Integer id, AddHotGameRequest request);
    void removeHotGame(Integer id);
    List<HotGameItem> fetchAllAvailableGames();
    HotGameItem getHotGameById(Integer id);
}
