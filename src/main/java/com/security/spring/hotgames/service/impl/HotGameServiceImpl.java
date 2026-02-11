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

    private static final Comparator<HotGameItem> HOT_GAME_COMPARATOR = Comparator
            .comparing((HotGameItem a) -> a.getProviderSortOrder() != null ? a.getProviderSortOrder() : 9999)
            .thenComparing(a -> a.getSortOrder() != null ? a.getSortOrder() : 9999);

    @Override
    public Map<String, List<ProviderGame>> getHotGames() {
        List<HotGameItem> items = hotGameItemRepository.findAll();

        // Group by category and sort within groups
        return items.stream()
                .collect(Collectors.groupingBy(HotGameItem::getCategory, 
                        LinkedHashMap::new, 
                        Collectors.collectingAndThen(Collectors.toList(), list -> {
                            list.sort(HOT_GAME_COMPARATOR);
                            String category = list.get(0).getCategory();
                            
                            if ("hotLiveCasino".equals(category)) {
                                List<ProviderGame> providers = list.stream()
                                        .filter(item -> "PROVIDER".equalsIgnoreCase(item.getItemType()))
                                        .map(this::mapToProviderGame)
                                        .collect(Collectors.toList());
                                
                                if (!providers.isEmpty()) return providers;
                                
                                // Fallback: deduplicate by product code if no PROVIDER entries exist
                                return list.stream()
                                        .filter(item -> item.getProductCode() != null)
                                        .collect(Collectors.toMap(
                                                HotGameItem::getProductCode,
                                                item -> item,
                                                (existing, replacement) -> existing,
                                                LinkedHashMap::new
                                        ))
                                        .values().stream()
                                        .map(this::mapToProviderGame)
                                        .collect(Collectors.toList());
                            }
                            return list.stream().map(this::mapToProviderGame).collect(Collectors.toList());
                        })))
                .entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

    @Override
    public List<HotGameItem> getHotGameItems() {
        List<HotGameItem> items = hotGameItemRepository.findAll();
        items.sort(Comparator.comparing(HotGameItem::getCategory).thenComparing(HOT_GAME_COMPARATOR));
        return items;
    }

    @Override
    @Transactional
    public void refreshHotGames() {
        List<HotGameItem> allItems = hotGameItemRepository.findAll();
        Set<ProductTypeKey> keys = allItems.stream()
                .filter(item -> item.getProductCode() != null && item.getGameType() != null)
                .map(item -> new ProductTypeKey(item.getProductCode(), item.getGameType()))
                .collect(Collectors.toSet());

        List<HotGameItem> itemsToUpdateTotal = new java.util.ArrayList<>();

        for (ProductTypeKey key : keys) {
            try {
                GetGameListResponse response = getGameListService.getGameListConfigSystem(GetGameListRequest.builder()
                        .productID(key.productId)
                        .gameType(key.gameType)
                        .fromHotGame(true)
                        .build());
                
                if (response != null && response.getGameListResponse() != null && response.getGameListResponse().getProviderGames() != null) {
                    Map<String, ProviderGame> freshMap = response.getGameListResponse().getProviderGames().stream()
                            .collect(Collectors.toMap(ProviderGame::getGameCode, g -> g, (e, r) -> e));

                    List<HotGameItem> targets = allItems.stream()
                            .filter(item -> Objects.equals(item.getProductCode(), key.productId) 
                                    && Objects.equals(item.getGameType(), key.gameType)
                                    && freshMap.containsKey(item.getGameCode()))
                            .collect(Collectors.toList());

                    for (HotGameItem item : targets) {
                        updateItemDetails(item, freshMap.get(item.getGameCode()));
                    }
                    itemsToUpdateTotal.addAll(targets);
                }
            } catch (Exception e) {
                log.error("Failed to refresh games for product {} type {}", key.productId, key.gameType, e);
            }
        }
        
        if (!itemsToUpdateTotal.isEmpty()) {
            hotGameItemRepository.saveAll(itemsToUpdateTotal);
            log.info("Refreshed {} hot game items", itemsToUpdateTotal.size());
        }
    }

    @Override
    @Transactional
    public void addHotGame(AddHotGameRequest request) {
        if (hotGameItemRepository.existsByCategoryAndGameCode(request.getCategory(), request.getGameCode())) {
            log.warn("Hot game already exists in category {}: {}", request.getCategory(), request.getGameCode());
            return;
        }

        GameSoftGameProvider provider = gameProviderRepo.findByProduct(request.getProductCode().longValue()).orElse(null);
        int providerSort = (provider != null && provider.getSortNumber() != null) ? provider.getSortNumber() : 9999;

        HotGameItem item = HotGameItem.builder()
                .category(request.getCategory())
                .gameCode(request.getGameCode())
                .gameType(request.getGameType())
                .productId(request.getProductId() == null ? 0 : request.getProductId())
                .productCode(request.getProductCode())
                .sortOrder(request.getSortOrder())
                .itemType("GAME")
                .providerSortOrder(providerSort)
                .build();

        // Immediately fetch details if available
        try {
            GetGameListResponse response = getGameListService.getGameListConfigSystem(GetGameListRequest.builder()
                    .productID(request.getProductCode())
                    .gameType(request.getGameType())
                    .fromHotGame(true)
                    .build());
            
            if (response != null && response.getGameListResponse() != null && response.getGameListResponse().getProviderGames() != null) {
                response.getGameListResponse().getProviderGames().stream()
                        .filter(g -> Objects.equals(g.getGameCode(), request.getGameCode()))
                        .findFirst()
                        .ifPresent(fresh -> updateItemDetails(item, fresh));
            }
        } catch (Exception e) {
            log.warn("Could not fetch immediate details for new game {}: {}", request.getGameCode(), e.getMessage());
        }

        hotGameItemRepository.save(item);
    }

    @Override
    @Transactional
    public void removeHotGame(Integer id) {
        hotGameItemRepository.deleteById(id);
    }

    @Override
    public List<HotGameItem> fetchAllAvailableGames() {
        List<GameSoftGameProvider> providers = gameProviderRepo.findAll();
        List<HotGameItem> allItems = new java.util.ArrayList<>();

        for (GameSoftGameProvider provider : providers) {
            if (provider.getGameType() == null || provider.getProduct() == null) continue;
            
            String gameTypeCode = provider.getGameType().getCode();
            boolean isLiveCasino = "LIVE_CASINO".equalsIgnoreCase(gameTypeCode);
            
            // Add Provider entry
            allItems.add(HotGameItem.builder()
                    .gameName(provider.getProductCode() + " Provider")
                    .gameCode(provider.getProductCode() + "_PROV")
                    .gameType(gameTypeCode)
                    .productCode(provider.getProduct().intValue())
                    .status("ACTIVATED")
                    .category(isLiveCasino ? "hotLiveCasino" : provider.getProductCode() + " - " + gameTypeCode)
                    .itemType("PROVIDER")
                    .providerSortOrder(provider.getSortNumber() != null ? provider.getSortNumber() : 9999)
                    .sortOrder(0)
                    .gameTypeId(provider.getGameType().getId())
                    .gameTypeName(provider.getGameType().getDescription())
                    .conversionRate(1.0)
                    .build());

            // Fetch individual games
            try {
                GetGameListResponse response = getGameListService.getGameListConfigSystem(GetGameListRequest.builder()
                        .productID(provider.getProduct().intValue())
                        .gameType(gameTypeCode)
                        .fromHotGame(true)
                        .build());
                
                if (response != null && response.getGameListResponse() != null && response.getGameListResponse().getProviderGames() != null) {
                    for (ProviderGame pg : response.getGameListResponse().getProviderGames()) {
                        allItems.add(HotGameItem.builder()
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
                                .category(isLiveCasino ? "hotLiveCasino" : provider.getProductCode() + " - " + gameTypeCode)
                                .itemType("GAME")
                                .providerSortOrder(provider.getSortNumber() != null ? provider.getSortNumber() : 9999)
                                .sortOrder(1)
                                .gameTypeId(pg.getGameTypeId())
                                .gameTypeName(pg.getGameTypeName())
                                .conversionRate(pg.getConversionRate())
                                .build());
                    }
                }
            } catch (Exception e) {
                log.error("Failed to fetch games for provider {} type {}", provider.getProduct(), gameTypeCode, e);
            }
        }
        
        // Handle GameBank (product 2026) fallback
        if (providers.stream().noneMatch(p -> p.getProduct() != null && p.getProduct() == 2026)) {
             try {
                GetGameListResponse gbResponse = getGameListService.getGameListConfigSystem(GetGameListRequest.builder()
                        .productID(2026).gameType("SLOT").fromHotGame(true).build());
                if (gbResponse != null && gbResponse.getGameListResponse() != null && gbResponse.getGameListResponse().getProviderGames() != null) {
                    for (ProviderGame pg : gbResponse.getGameListResponse().getProviderGames()) {
                        allItems.add(HotGameItem.builder()
                                .gameName(pg.getGameName()).gameCode(pg.getGameCode()).gameType(pg.getGameType())
                                .productCode(2026).imageUrl(pg.getImageUrl()).status(pg.getStatus())
                                .category("GameBank - SLOT").itemType("GAME").providerSortOrder(1).sortOrder(1)
                                .gameTypeId(pg.getGameTypeId()).gameTypeName(pg.getGameTypeName()).conversionRate(pg.getConversionRate())
                                .build());
                    }
                }
             } catch (Exception e) {
                 log.error("Failed to fetch games for GameBank", e);
             }
        }

        allItems.sort(Comparator.comparing(HotGameItem::getCategory).thenComparing(HOT_GAME_COMPARATOR).thenComparing(a -> a.getGameName() != null ? a.getGameName() : ""));
        return allItems;
    }

    private void updateItemDetails(HotGameItem item, ProviderGame fresh) {
        item.setGameName(fresh.getGameName());
        item.setImageUrl(fresh.getImageUrl());
        item.setSupportCurrency(fresh.getSupportCurrency());
        item.setStatus(fresh.getStatus());
        item.setPlatform(fresh.getPlatform());
        item.setGameUrl(fresh.getGameUrl());
        item.setDescription(fresh.getDescription());
        item.setProductCode(fresh.getProductCode() == 0 ? 2026 : fresh.getProductCode()); // Ensure product code is set to 2026 if missing
        item.setGameTypeId(fresh.getGameTypeId());
        item.setGameTypeName(fresh.getGameTypeName());
        item.setConversionRate(fresh.getConversionRate());
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
                .gameTypeId(item.getGameTypeId())
                .gameTypeName(item.getGameTypeName())
                .conversionRate(item.getConversionRate())
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