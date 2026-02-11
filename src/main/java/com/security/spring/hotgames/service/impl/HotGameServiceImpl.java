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
import com.security.spring.thirdpartygames.gameType.repo.GameTypeRepo;
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
            .comparing((HotGameItem a) -> a.getSortOrder() != null ? a.getSortOrder() : 9999);

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
                        .productID(key.productCode)
                        .gameType(key.gameType)
                        .fromHotGame(true)
                        .build());
                
                if (response != null && response.getGameListResponse() != null && response.getGameListResponse().getProviderGames() != null) {
                    Map<String, ProviderGame> freshMap = response.getGameListResponse().getProviderGames().stream()
                            .collect(Collectors.toMap(ProviderGame::getGameCode, g -> g, (e, r) -> e));

                    List<HotGameItem> targets = allItems.stream()
                            .filter(item -> Objects.equals(item.getProductCode(), key.productCode) 
                                    && Objects.equals(item.getGameType(), key.gameType)
                                    && freshMap.containsKey(item.getGameCode()))
                            .collect(Collectors.toList());

                    for (HotGameItem item : targets) {
                        updateItemDetails(item, freshMap.get(item.getGameCode()));
                    }
                    itemsToUpdateTotal.addAll(targets);
                }
            } catch (Exception e) {
                log.error("Failed to refresh games for product {} type {}", key.productCode, key.gameType, e);
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

        HotGameItem item = HotGameItem.builder()
                .category(request.getCategory())
                .gameCode(request.getGameCode())
                .gameType(request.getGameType())
                .productCode(request.getProductCode())
                .sortOrder(request.getSortOrder())
                .itemType("GAME")
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
    public void updateHotGame(Integer id, AddHotGameRequest request) {
        HotGameItem item = hotGameItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hot game not found with id: " + id));

        item.setCategory(request.getCategory());
        item.setGameCode(request.getGameCode());
        item.setGameType(request.getGameType());
        item.setProductCode(request.getProductCode());
        item.setSortOrder(request.getSortOrder());

        // Fetch details to ensure they are up to date
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
            log.warn("Could not fetch details for updated game {}: {}", request.getGameCode(), e.getMessage());
        }

        hotGameItemRepository.save(item);
    }

    @Override
    @Transactional
    public void removeHotGame(Integer id) {
        hotGameItemRepository.deleteById(id);
    }

    @Override
    public HotGameItem getHotGameById(Integer id) {
        return hotGameItemRepository.findById(id).orElse(null);
    }

    @Override
    public List<HotGameItem> fetchAllAvailableGames() {
        List<GameSoftGameProvider> providers = gameProviderRepo.findAll();
        List<HotGameItem> allItems = new java.util.ArrayList<>();

        for (GameSoftGameProvider provider : providers) {
            if (provider.getGameType() == null || provider.getProduct() == null) continue;
            
            String gameTypeCode = provider.getGameType().getCode();
            Integer currentProviderProduct = provider.getProduct().intValue(); // This will be 2026 for GameBank
            boolean isLiveCasino = "LIVE_CASINO".equalsIgnoreCase(gameTypeCode);
            
            // 1. Add Provider entry
            allItems.add(HotGameItem.builder()
                    .gameName(provider.getProductCode() + " Provider")
                    .gameCode(provider.getProductCode() + "_PROV")
                    .gameType(gameTypeCode)
                    .productCode(currentProviderProduct)
                    .status("ACTIVATED")
                    .category(isLiveCasino ? "hotLiveCasino" : provider.getProductCode() + " - " + gameTypeCode)
                    .itemType("PROVIDER")
                    .sortOrder(0)
                    .gameTypeId(provider.getGameType().getId())
                    .gameTypeName(provider.getGameType().getDescription())
                    .conversionRate(1.0)
                    .build());

            // 2. Fetch individual games
            try {
                GetGameListResponse response = getGameListService.getGameListConfigSystem(GetGameListRequest.builder()
                        .productID(currentProviderProduct)
                        .gameType(gameTypeCode)
                        .fromHotGame(true)
                        .build());
                
                if (response != null && response.getGameListResponse() != null && response.getGameListResponse().getProviderGames() != null) {
                    for (ProviderGame pg : response.getGameListResponse().getProviderGames()) {
                        allItems.add(HotGameItem.builder()
                                .gameName(pg.getGameName())
                                .gameCode(pg.getGameCode())
                                .gameType(pg.getGameType())
                                // FIX HERE: If the individual game returns 0, force it to the current provider's code (e.g., 2026)
                                .productCode((pg.getProductCode() == 0) ? currentProviderProduct : pg.getProductCode())
                                .imageUrl(pg.getImageUrl())
                                .supportCurrency(pg.getSupportCurrency())
                                .status(pg.getStatus())
                                .platform(pg.getPlatform())
                                .gameUrl(pg.getGameUrl())
                                .description(pg.getDescription())
                                .category(isLiveCasino ? "hotLiveCasino" : provider.getProductCode() + " - " + gameTypeCode)
                                .itemType("GAME")
                                .sortOrder(1)
                                .gameTypeId(pg.getGameTypeId())
                                .gameTypeName(pg.getGameTypeName())
                                .conversionRate(pg.getConversionRate())
                                .build());
                    }
                }
            } catch (Exception e) {
                log.error("Failed to fetch games for provider {} type {}", currentProviderProduct, gameTypeCode, e);
            }
        }
        
        // ... rest of your GameBank fallback logic (already has the fix)
        
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
        final Integer productCode;
        final String gameType;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ProductTypeKey that = (ProductTypeKey) o;
            return Objects.equals(productCode, that.productCode) && Objects.equals(gameType, that.gameType);
        }

        @Override
        public int hashCode() {
            return Objects.hash(productCode, gameType);
        }
    }
}