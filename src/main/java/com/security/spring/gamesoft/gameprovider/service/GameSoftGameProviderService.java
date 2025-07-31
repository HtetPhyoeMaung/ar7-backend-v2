package com.security.spring.gamesoft.gameprovider.service;

import com.security.spring.gamesoft.gameType.entity.GameType;
import com.security.spring.gamesoft.gameprovider.dto.GameProviderResponse;
import com.security.spring.gamesoft.gameprovider.entity.GameSoftGameProvider;
import com.security.spring.rro.GameSoftProviderRequest;

import java.io.IOException;

public interface GameSoftGameProviderService {
     GameProviderResponse saveGameProvider(GameSoftProviderRequest data) throws IOException;
     GameProviderResponse getAllGameProvider();
     GameProviderResponse providerFindById(Integer providerId);
     GameProviderResponse updateGameProvider(int id,GameSoftProviderRequest data) throws IOException;
     GameSoftGameProvider findByProductAndGameType(Long productID, GameType gameType);
     GameProviderResponse getGameProviderByGameType(Integer gameTypeId);

     GameProviderResponse deleteProviderById(int providerId);
}
