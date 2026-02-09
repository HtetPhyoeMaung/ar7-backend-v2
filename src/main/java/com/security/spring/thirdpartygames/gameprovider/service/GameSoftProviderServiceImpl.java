package com.security.spring.thirdpartygames.gameprovider.service;

import com.security.spring.exceptionall.DataNotFoundException;
import com.security.spring.thirdpartygames.gameType.entity.GameType;
import com.security.spring.thirdpartygames.gameType.repo.GameTypeRepo;
import com.security.spring.thirdpartygames.gameprovider.dto.GameProviderObj;
import com.security.spring.thirdpartygames.gameprovider.dto.GameProviderResponse;
import com.security.spring.thirdpartygames.gameprovider.dto.ProviderSortItem;
import com.security.spring.thirdpartygames.gameprovider.dto.SortGameProviderRequest;
import com.security.spring.thirdpartygames.gameprovider.entity.GameSoftGameProvider;
import com.security.spring.thirdpartygames.gameprovider.repository.GameProviderRepo;
import com.security.spring.thirdpartygames.getProviderList.dto.ProviderDataFeign;
import com.security.spring.thirdpartygames.getProviderList.dto.ProviderResponse;
import com.security.spring.rro.GameSoftProviderRequest;
import com.security.spring.storage.StorageService;
import com.security.spring.utils.ConstantInformationForGameSoft;
import com.security.spring.utils.SignUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class GameSoftProviderServiceImpl implements GameSoftGameProviderService {


    private final GameProviderRepo gameProviderRepo;
    private final StorageService storageService;
    private final GameTypeRepo gameTypeRepo;
    private final RestTemplate restTemplate;
    private final ConstantInformationForGameSoft constantDataObj;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    @Override
    @Transactional
    public GameProviderResponse saveGameProvider(GameSoftProviderRequest data) throws IOException {

        var gameType = gameTypeRepo.findByCode(data.getGameTypeCode()).orElseThrow(() ->
                new DataNotFoundException("Game Type Not Found By " + data.getGameTypeCode()));
        GameSoftGameProvider saveObj = GameSoftGameProvider
                .builder()
                .product(data.getProduct())
                .productCode(data.getProductCode())
                .imageName(storageService.uploadImage(data.getImage()))
                .currencyCode(data.getCurrencyCode())
                .conversionRate(data.getConversionRate() != null ? data.getConversionRate() : 1.0)
                .gameType(gameType)
                .build();

        gameProviderRepo.save(saveObj);

        return GameProviderResponse
                .builder()
                .message("Save Success Game Provider")
                .statusCode(HttpStatus.OK.value())
                .status(true)
                .build();
    }

    @Override
    @Transactional
    public GameProviderResponse getAllGameProvider() {
        List<GameSoftGameProvider> gameProviderList = gameProviderRepo.findAll()
                .stream()
                .filter(gp -> !gp.isDeleted())
                .sorted(Comparator.comparing(GameSoftGameProvider::getSortNumber, Comparator.nullsLast(Comparator.naturalOrder()))
                        .thenComparing(GameSoftGameProvider::getId))
                .toList();
        List<GameProviderObj> gameProviderObjList = gameProviderList
                .stream()
                .map(
                        obj -> GameProviderObj
                                .builder()
                                .id(obj.getId())
                                .product(obj.getProduct())
                                .productCode(obj.getProductCode())
                                .gameTypeId(obj.getGameType().getId())
                                .imageUrl(obj.getImageName()==null?null:storageService
                                        .getImageByName(obj.getImageName()))
                                .gameTypeName(obj.getGameType().getDescription())
                                .currencyCode(obj.getCurrencyCode())
                                .conversionRate(obj.getConversionRate())
                                .sortNumber(obj.getSortNumber())
                                .build()
                )
                .toList();

        return GameProviderResponse
                .builder()
                .status(true)
                .message("API Good Working")
                .statusCode(HttpStatus.OK.value())
                .gameProviderObjList(gameProviderObjList)
                .build();
    }

    @Override
    @Transactional
    public GameProviderResponse providerFindById(Integer providerId) {
        Optional<GameSoftGameProvider> gameProvider = gameProviderRepo.findById(providerId);
        if (gameProvider.isEmpty() || gameProvider.get().isDeleted()) {
            throw new DataNotFoundException("Game Provider Not Found");
        }

        List<GameProviderObj> gameProviderObjList = gameProvider
                .stream()
                .map(obj -> GameProviderObj
                        .builder()
                        .id(obj.getId())
                        .product(obj.getProduct())
                        .productCode(obj.getProductCode())
                        .gameTypeId(obj.getGameType().getId())
                        .imageUrl(obj.getImageName()==null?null:storageService
                                .getImageByName(obj.getImageName()))
                        .gameTypeName(obj.getGameType().getDescription())
                                .currencyCode(obj.getCurrencyCode())
                                .conversionRate(obj.getConversionRate())
                                .sortNumber(obj.getSortNumber())
                                .build())
                .toList();

        return GameProviderResponse
                .builder()
                .status(true)
                .message("API Good Working")
                .statusCode(HttpStatus.OK.value())
                .gameProviderObjList(gameProviderObjList)
                .build();
    }

    @Override
    public GameProviderResponse updateGameProvider(int id,GameSoftProviderRequest data) throws IOException {
        GameType gameType = null;
        if(data.getGameTypeCode() != null){
            gameType = gameTypeRepo.findByCode(data.getGameTypeCode()).orElseThrow(()->
                    new DataNotFoundException("Game Type Not Found"));
        }

        GameSoftGameProvider updateObj = gameProviderRepo.findById(id).orElseThrow(()->
              new DataNotFoundException("Game Provider Not Found"));
        updateObj.setProduct(data.getProduct() != null ? data.getProduct() : updateObj.getProduct());
        updateObj.setProductCode(data.getProductCode() != null ? data.getProductCode() : updateObj.getProductCode());
        updateObj.setCurrencyCode(data.getCurrencyCode() != null ? data.getCurrencyCode() : updateObj.getCurrencyCode());
        updateObj.setConversionRate(data.getConversionRate() != null ? data.getConversionRate() : updateObj.getConversionRate());
        updateObj.setGameType(gameType != null ? gameType : updateObj.getGameType());
        if (data.getImage()!=null){
            if(updateObj.getImageName() == null || updateObj.getImageName().isEmpty()){
                updateObj.setImageName(storageService.uploadImage(data.getImage()));
            }else{
                updateObj.setImageName(storageService.updateImage(data.getImage(),updateObj.getImageName()));
            }
        }
        gameProviderRepo.save(updateObj);

        return GameProviderResponse
                .builder()
                .message("Update Successfully")
                .statusCode(HttpStatus.OK.value())
                .status(true)
                .build();
    }

    @Override
    public GameSoftGameProvider findByProductAndGameType(Long productID, GameType gameType) {
        Optional<GameSoftGameProvider> gameProvider = gameProviderRepo.findByProductAndGameType(productID, gameType);
        if (gameProvider.isEmpty()) {
            throw new DataNotFoundException("GameProvider Not Found By " + productID + "and "+ gameType.getCode());
        }

         return gameProvider.get();
    }

    @Override
    @Transactional
    public GameProviderResponse getGameProviderByGameType(Integer gameTypeId) {
        Optional<GameType> gameType = gameTypeRepo.findById(gameTypeId);
        if(gameType.isEmpty()){
            throw new DataNotFoundException("Game Type Id Wrong");
        }
        GameType gameTypeGet = gameType.get();
        List<GameSoftGameProvider> gameProviderList = gameProviderRepo.findByGameType(gameTypeGet)
                .stream()
                .filter(gp -> !gp.isDeleted())
                .sorted(Comparator.comparing(GameSoftGameProvider::getSortNumber, Comparator.nullsLast(Comparator.naturalOrder()))
                        .thenComparing(GameSoftGameProvider::getId))
                .toList();

        List<GameProviderObj> gameProviderObjList = gameProviderList
                .stream()
                .map(
                        obj -> GameProviderObj
                                .builder()
                                .id(obj.getId())
                                .product(obj.getProduct())
                                .productCode(obj.getProductCode())
                                .gameTypeId(obj.getGameType().getId())
                                .imageUrl(obj.getImageName()==null?null:storageService
                                        .getImageByName(obj.getImageName()))
                                .gameTypeName(obj.getGameType().getDescription())
                                .gameTypeCode(obj.getGameType().getCode())
                                .currencyCode(obj.getCurrencyCode())
                                .conversionRate(obj.getConversionRate())
                                .sortNumber(obj.getSortNumber())
                                .build()
                )
                .collect(Collectors.toList());


        return GameProviderResponse
                .builder()
                .status(true)
                .statusCode(HttpStatus.OK.value())
                .message("Game Provider List By Game Type")
                .gameProviderObjList(gameProviderObjList)
                .build();
    }

    @Override
    public GameProviderResponse deleteProviderById(int providerId) {
        var gameProvider = gameProviderRepo.findById(providerId).orElseThrow(()->
                new DataNotFoundException("Provider not found by ID : "+providerId));
        gameProvider.setDeleted(true);
        gameProviderRepo.save(gameProvider);
        return GameProviderResponse
                .builder()
                .status(true)
                .statusCode(HttpStatus.OK.value())
                .message("Successfully deleted provider by ID : "+providerId)
                .build();
    }

    @Override
    @Transactional
    public GameProviderResponse syncProviders() {
        String methodName = "productlist";
        String requestTime = LocalDateTime.now().format(formatter);
        String operatorCode = constantDataObj.getOperatorCode();
        String secretKey = constantDataObj.getSecretKey();
        String apiUrl = constantDataObj.getApiUrl();
        String thirdPartyRoute = apiUrl + "/api/operators/available-products";

        String sign = SignUtil.createSignatureForRequest(operatorCode, Long.parseLong(requestTime), methodName, secretKey);
        URI uri = UriComponentsBuilder.fromHttpUrl(thirdPartyRoute)
                .queryParam("operator_code", operatorCode)
                .queryParam("request_time", requestTime)
                .queryParam("sign", sign)
                .build()
                .toUri();

        log.info("Sync Providers Request URI: {}", uri);

        ResponseEntity<List<ProviderDataFeign>> response;
        int createdCount = 0;
        int updatedCount = 0;
        int skippedCount = 0;

        try {
            response = restTemplate.exchange(
                    uri,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<ProviderDataFeign>>() {
                    });

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                List<ProviderDataFeign> providerListFromApi = response.getBody();
                log.info("Received {} providers from API", providerListFromApi.size());

                for (ProviderDataFeign providerDataFeign : providerListFromApi) {
                    try {
                        // Convert ProviderDataFeign to ProviderResponse.ProviderData
                        ProviderResponse.ProviderData providerData = ProviderResponse.ProviderData.of(providerDataFeign);

                        // Find or create GameType based on game_type from API
                        Optional<GameType> gameTypeOpt = gameTypeRepo.findByCode(providerData.getGameType());
                        if (gameTypeOpt.isEmpty()) {
                            log.warn("GameType not found for code: {}. Skipping provider: {}", 
                                    providerData.getGameType(), providerData.getProductName());
                            skippedCount++;
                            continue;
                        }
                        GameType gameType = gameTypeOpt.get();

                        // Check if provider already exists (by product_id and game_type)
                        Optional<GameSoftGameProvider> existingProviderOpt = 
                                gameProviderRepo.findByProductAndGameType(providerData.getProductCode(), gameType);

                        GameSoftGameProvider gameProvider;
                        if (existingProviderOpt.isPresent()) {
                            // Update existing provider
                            gameProvider = existingProviderOpt.get();
                            // Update fields from API response
                            gameProvider.setCurrencyCode(providerData.getCurrency());
                            gameProvider.setProductCode(providerData.getProductName());
                            
                            // Update conversion rate based on currency
                            try {
                                com.security.spring.thirdpartygames.callback.dto.Currency currency = 
                                        com.security.spring.thirdpartygames.callback.dto.Currency.valueOf(providerData.getCurrency());
                                gameProvider.setConversionRate(currency.getRate().doubleValue());
                            } catch (IllegalArgumentException e) {
                                log.warn("Currency not found: {}. Using default rate 1.0", providerData.getCurrency());
                                gameProvider.setConversionRate(1.0);
                            }
                            
                            gameProvider.setDeleted(false); // Ensure it's not marked as deleted
                            updatedCount++;
                        } else {
                            // Create new provider using the entity's of method
                            gameProvider = GameSoftGameProvider.of(providerData, gameType);
                            createdCount++;
                        }

                        gameProviderRepo.save(gameProvider);
                    } catch (Exception e) {
                        log.error("Error processing provider: {}", providerDataFeign.getProductName(), e);
                        skippedCount++;
                    }
                }

                String message = String.format("Sync completed. Created: %d, Updated: %d, Skipped: %d", 
                        createdCount, updatedCount, skippedCount);
                log.info(message);

                return GameProviderResponse
                        .builder()
                        .status(true)
                        .statusCode(HttpStatus.OK.value())
                        .message(message)
                        .build();
            } else {
                return GameProviderResponse
                        .builder()
                        .status(false)
                        .statusCode(response.getStatusCode().value())
                        .message("Failed to fetch providers from API")
                        .build();
            }
        } catch (Exception ex) {
            log.error("Error syncing providers", ex);
            String errorMsg = "Error syncing providers: " + ex.getMessage();
            return GameProviderResponse.builder()
                    .status(false)
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(errorMsg)
                    .build();
        }
    }

    @Override
    @Transactional
    public GameProviderResponse sortGameProviders(SortGameProviderRequest request) {
        GameType gameType = gameTypeRepo.findByCode(request.getGameTypeCode())
                .orElseThrow(() -> new DataNotFoundException("Game type not found: " + request.getGameTypeCode()));

        int updatedCount = 0;
        for (ProviderSortItem item : request.getProviders()) {
            if (item.getProduct() == null || item.getSortNumber() == null) {
                continue;
            }
            Optional<GameSoftGameProvider> providerOpt = gameProviderRepo.findByProductAndGameType(item.getProduct(), gameType);
            if (providerOpt.isPresent()) {
                GameSoftGameProvider provider = providerOpt.get();
                provider.setSortNumber(item.getSortNumber());
                gameProviderRepo.save(provider);
                updatedCount++;
            } else {
                log.warn("Provider not found for product {} in game type {}", item.getProduct(), request.getGameTypeCode());
            }
        }

        String message = String.format("Sort order updated for %d providers in game type %s", updatedCount, request.getGameTypeCode());
        return GameProviderResponse
                .builder()
                .status(true)
                .statusCode(HttpStatus.OK.value())
                .message(message)
                .build();
    }

    @Override
    @Transactional
    public GameProviderResponse sortGameProvidersWithDefaults() {
        Map<String, List<ProviderSortItem>> defaultOrder = getDefaultSortOrderMap();
        int totalUpdated = 0;
        for (Map.Entry<String, List<ProviderSortItem>> entry : defaultOrder.entrySet()) {
            String gameTypeCode = entry.getKey();
            Optional<GameType> gameTypeOpt = gameTypeRepo.findByCode(gameTypeCode);
            if (gameTypeOpt.isEmpty()) {
                log.debug("Game type not found, skipping: {}", gameTypeCode);
                continue;
            }
            GameType gameType = gameTypeOpt.get();
            for (ProviderSortItem sortItem : entry.getValue()) {
                if (sortItem.getProduct() == null || sortItem.getSortNumber() == null) continue;
                Optional<GameSoftGameProvider> providerOpt = gameProviderRepo.findByProductAndGameType(sortItem.getProduct(), gameType);
                if (providerOpt.isPresent()) {
                    GameSoftGameProvider provider = providerOpt.get();
                    provider.setSortNumber(sortItem.getSortNumber());
                    gameProviderRepo.save(provider);
                    totalUpdated++;
                }
            }
        }
        String message = String.format("Default sort order applied. Updated %d providers.", totalUpdated);
        return GameProviderResponse
                .builder()
                .status(true)
                .statusCode(HttpStatus.OK.value())
                .message(message)
                .build();
    }

    private static Map<String, List<ProviderSortItem>> getDefaultSortOrderMap() {
        Map<String, List<ProviderSortItem>> map = new HashMap<>();
        map.put("SLOT", List.of(
                new ProviderSortItem(1006L, 1), new ProviderSortItem(1018L, 2), new ProviderSortItem(1079L, 3), new ProviderSortItem(1091L, 4),
                new ProviderSortItem(1085L, 5), new ProviderSortItem(1009L, 6), new ProviderSortItem(1204L, 7), new ProviderSortItem(2026L, 8)));
        map.put("FISHING", List.of(
                new ProviderSortItem(1091L, 1), new ProviderSortItem(1009L, 2), new ProviderSortItem(1085L, 3), new ProviderSortItem(1079L, 4), new ProviderSortItem(1225L, 5)));
        map.put("18", List.of(new ProviderSortItem(2026L, 1)));
        map.put("LIVE_CASINO", List.of(
                new ProviderSortItem(1022L, 1), new ProviderSortItem(1006L, 2), new ProviderSortItem(1091L, 3), new ProviderSortItem(1149L, 4)));
        map.put("SHAN_BUGYI", List.of(new ProviderSortItem(2026L, 1)));
        map.put("SPORT_BOOK", List.of(new ProviderSortItem(1012L, 1), new ProviderSortItem(1222L, 2)));
        map.put("ESPORT", List.of(new ProviderSortItem(1222L, 1)));
        return map;
    }

}
