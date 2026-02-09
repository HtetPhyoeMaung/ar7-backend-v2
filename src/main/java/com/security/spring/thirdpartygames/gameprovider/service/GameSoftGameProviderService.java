package com.security.spring.thirdpartygames.gameprovider.service;

import com.security.spring.thirdpartygames.gameType.entity.GameType;
import com.security.spring.thirdpartygames.gameprovider.dto.GameProviderResponse;
import com.security.spring.thirdpartygames.gameprovider.dto.SortGameProviderRequest;
import com.security.spring.thirdpartygames.gameprovider.entity.GameSoftGameProvider;
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

     GameProviderResponse syncProviders();

     GameProviderResponse sortGameProviders(SortGameProviderRequest request);

     GameProviderResponse sortGameProvidersWithDefaults();
}
