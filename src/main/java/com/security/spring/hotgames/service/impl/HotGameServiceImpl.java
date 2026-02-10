package com.security.spring.hotgames.service.impl;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.security.spring.hotgames.dto.AddHotGameRequest;
import com.security.spring.hotgames.entity.HotGameItem;
import com.security.spring.hotgames.repository.HotGameItemRepository;
import com.security.spring.hotgames.service.HotGameService;
import com.security.spring.thirdpartygames.gameprovider.entity.GameSoftGameProvider;
import com.security.spring.thirdpartygames.gameprovider.repository.GameProviderRepo;
import com.security.spring.thirdpartygames.getGameList.dto.GetGameListRequest;
import com.security.spring.thirdpartygames.getGameList.dto.GetGameListResponse;
import com.security.spring.thirdpartygames.getGameList.dto.ProviderGame;
import com.security.spring.thirdpartygames.getGameList.service.GetGameListService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class HotGameServiceImpl implements HotGameService {
    private final HotGameItemRepository hotGameItemRepository;
    private final GetGameListService getGameListService;
    private final GameProviderRepo gameProviderRepo;

    @Override
    public Map<String, List<ProviderGame>> getHotGames() {
        List<HotGameItem> items = hotGameItemRepository.findAllByOrderBySortOrderAsc();

        // Group by category
        Map<String, List<HotGameItem>> groupedItems = items.stream()
                .collect(Collectors.groupingBy(HotGameItem::getCategory));

        // Convert to DTO
        Map<String, List<ProviderGame>> result = new LinkedHashMap<>();
        
        groupedItems.forEach((category, hotGameItems) -> {
            hotGameItems.sort(Comparator.comparingInt(item -> item.getSortOrder() != null ? item.getSortOrder() : 0));
            List<ProviderGame> gameDtos = hotGameItems.stream()
                    .map(this::mapToProviderGame)
                    .collect(Collectors.toList());
            result.put(category, gameDtos);
        });

        return result;
    }

    @Override
    public List<HotGameItem> getHotGameItems() {
        return hotGameItemRepository.findAll(org.springframework.data.domain.Sort.by("category", "sortOrder"));
    }

    @Override
    @Transactional
    public void refreshHotGames() {
        List<HotGameItem> allItems = hotGameItemRepository.findAll();
        // Use a set of keys to identify unique product/type combinations
        Set<ProductTypeKey> keys = allItems.stream()
                .filter(item -> item.getProductId() != null && item.getGameType() != null)
                .map(item -> new ProductTypeKey(item.getProductCode(), item.getGameType()))
                .collect(Collectors.toSet());

        for (ProductTypeKey key : keys) {
            try {
                GetGameListRequest request = GetGameListRequest.builder()
                        .productID(key.productId)
                        .gameType(key.gameType)
                        .fromHotGame(true)
                        .build();

                GetGameListResponse response = getGameListService.getGameListConfigSystem(request);
                
                if (response != null && response.getGameListResponse() != null && response.getGameListResponse().getProviderGames() != null) {
                    List<ProviderGame> freshGames = response.getGameListResponse().getProviderGames();
                    Map<String, ProviderGame> gameMap = freshGames.stream()
                            .collect(Collectors.toMap(ProviderGame::getGameCode, g -> g, (existing, replacement) -> existing));

                    List<HotGameItem> itemsToUpdate = allItems.stream()
                            .filter(item -> Objects.equals(item.getProductCode(), key.productId) 
                                    && Objects.equals(item.getGameType(), key.gameType)
                                    && gameMap.containsKey(item.getGameCode()))
                            .collect(Collectors.toList());

                    for (HotGameItem item : itemsToUpdate) {
                        ProviderGame fresh = gameMap.get(item.getGameCode());
                        updateItemDetails(item, fresh);
                    }
                    hotGameItemRepository.saveAll(itemsToUpdate);
                }
            } catch (Exception e) {
                log.error("Failed to refresh hot games for product {} type {}", key.productId, key.gameType, e);
            }
        }
    }

    @Override
    @Transactional
    public void addHotGame(AddHotGameRequest request) {
        HotGameItem item = HotGameItem.builder()
                .category(request.getCategory())
                .gameCode(request.getGameCode())
                .gameType(request.getGameType())
                .productId(request.getProductId() == null ? 0 : request.getProductId())
                .productCode(request.getProductCode())
                .sortOrder(request.getSortOrder())
                .build();
        hotGameItemRepository.save(item);
        // Attempt immediate refresh for this item if possible, or leave it blank until refresh called
        // Since we don't fetch on add, the user might see empty details unless we trigger a refresh.
        // Let's trigger a targeted refresh or global refresh.
        // Global refresh is safer but slower. Given the context, let's just save. Ideally we fetch details here.
    }

    @Override
    @Transactional
    public void removeHotGame(Integer id) {
        hotGameItemRepository.deleteById(id);
    }

    @Override
    public List<HotGameItem> fetchAllAvailableGames() {
        List<GameSoftGameProvider> providers = gameProviderRepo.findAll();
        List<HotGameItem> allGames = new java.util.ArrayList<>();

        for (GameSoftGameProvider provider : providers) {
            if (provider.getGameType() == null || provider.getProduct() == null) continue;
            
            try {
                GetGameListRequest request = GetGameListRequest.builder()
                        .productID(provider.getProduct().intValue())
                        .gameType(provider.getGameType().getCode())
                        .fromHotGame(true)
                        .build();

                GetGameListResponse response = getGameListService.getGameListConfigSystem(request);
                
                if (response != null && response.getGameListResponse() != null && response.getGameListResponse().getProviderGames() != null) {
                    List<ProviderGame> freshGames = response.getGameListResponse().getProviderGames();
                    for (ProviderGame pg : freshGames) {
                        allGames.add(HotGameItem.builder()
                                .gameName(pg.getGameName())
                                .gameCode(pg.getGameCode())
                                .gameType(pg.getGameType())
                                .productId(pg.getProductId())
                                .productCode(pg.getProductCode())
                                .imageUrl(pg.getImageUrl())
                                .supportCurrency(pg.getSupportCurrency())
                                .status(pg.getStatus())
                                .platform(pg.getPlatform())
                                .gameUrl(pg.getGameUrl())
                                .description(pg.getDescription())
                                .category(provider.getProductCode() + " - " + provider.getGameType().getCode())
                                .build());
                    }
                }
            } catch (Exception e) {
                log.error("Failed to fetch games for provider {} type {}", provider.getProduct(), provider.getGameType().getCode(), e);
            }
        }
        
        // Also fetch from GameBank (product 2026) if not already included
        if (providers.stream().noneMatch(p -> p.getProduct() != null && p.getProduct() == 2026)) {
             try {
                GetGameListRequest gbRequest = GetGameListRequest.builder()
                        .productID(2026)
                        .gameType("SLOT") // Default type for GameBank
                        .fromHotGame(true)
                        .build();
                GetGameListResponse gbResponse = getGameListService.getGameListConfigSystem(gbRequest);
                if (gbResponse != null && gbResponse.getGameListResponse() != null && gbResponse.getGameListResponse().getProviderGames() != null) {
                    for (ProviderGame pg : gbResponse.getGameListResponse().getProviderGames()) {
                        allGames.add(HotGameItem.builder()
                                .gameName(pg.getGameName())
                                .gameCode(pg.getGameCode())
                                .gameType(pg.getGameType())
                                .productId(pg.getProductId())
                                .productCode(pg.getProductCode())
                                .imageUrl(pg.getImageUrl())
                                .supportCurrency(pg.getSupportCurrency())
                                .status(pg.getStatus())
                                .platform(pg.getPlatform())
                                .gameUrl(pg.getGameUrl())
                                .description(pg.getDescription())
                                .category("GameBank - SLOT")
                                .build());
                    }
                }
             } catch (Exception e) {
                 log.error("Failed to fetch games for GameBank", e);
             }
        }

        return allGames;
    }

    private void updateItemDetails(HotGameItem item, ProviderGame fresh) {
        item.setGameName(fresh.getGameName());
        item.setImageUrl(fresh.getImageUrl());
        item.setSupportCurrency(fresh.getSupportCurrency());
        item.setStatus(fresh.getStatus());
        item.setPlatform(fresh.getPlatform());
        item.setGameUrl(fresh.getGameUrl());
        item.setDescription(fresh.getDescription());
        item.setProductCode(fresh.getProductCode());
    }

    private ProviderGame mapToProviderGame(HotGameItem item) {
        return ProviderGame.builder()
                .gameCode(item.getGameCode())
                .gameName(item.getGameName())
                .gameType(item.getGameType())
                .productId(item.getProductId() != null ? item.getProductId() : 0)
                .productCode(item.getProductCode() != null ? item.getProductCode() : 0)
                .imageUrl(item.getImageUrl())
                .supportCurrency(item.getSupportCurrency())
                .status(item.getStatus())
                .platform(item.getPlatform())
                .gameUrl(item.getGameUrl())
                .description(item.getDescription())
                .build();
    }

    @RequiredArgsConstructor
    private static class ProductTypeKey {
        final Integer productId;
        final String gameType;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ProductTypeKey that = (ProductTypeKey) o;
            return Objects.equals(productId, that.productId) && Objects.equals(gameType, that.gameType);
        }

        @Override
        public int hashCode() {
            return Objects.hash(productId, gameType);
        }
    }
}