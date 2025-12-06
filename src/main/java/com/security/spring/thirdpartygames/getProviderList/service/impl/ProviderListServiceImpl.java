package com.security.spring.thirdpartygames.getProviderList.service.impl;

import com.security.spring.exceptionall.DataNotFoundException;
import com.security.spring.thirdpartygames.gameType.entity.GameType;
import com.security.spring.thirdpartygames.gameType.repo.GameTypeRepo;
import com.security.spring.thirdpartygames.gameprovider.entity.GameSoftGameProvider;
import com.security.spring.thirdpartygames.gameprovider.repository.GameProviderRepo;
import com.security.spring.thirdpartygames.getProviderList.dto.ProviderDataFeign;
import com.security.spring.thirdpartygames.getProviderList.dto.ProviderResponse;
import com.security.spring.thirdpartygames.getProviderList.service.ProviderListService;
import com.security.spring.utils.ConstantInformationForGameSoft;
import com.security.spring.utils.SignUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProviderListServiceImpl implements ProviderListService {
    private final RestTemplate restTemplate;

    private final GameTypeRepo gameTypeRepo;

    private final GameProviderRepo gameProviderRepo;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    private final List blackListGameTypeCodes = List.of("14", "18", "19", "Q", "JACKPOT" , "BONUS");
    ConstantInformationForGameSoft constantDataObj = ConstantInformationForGameSoft.builder().build();

    private final String operatorCode = constantDataObj.getOperatorCode();
    private final String secretKey = constantDataObj.getSecretKey();
    private final String apiUrl = constantDataObj.getApiUrl();
    private final String thirdPartyRoute = apiUrl + "/api/operators/available-products";

    @Override
    @Transactional
    public ResponseEntity<ProviderResponse> getProviderListByGameType(String gameType) {
        if (gameType!=null) {
            gameTypeRepo.findByCode(gameType).orElseThrow(() -> new DataNotFoundException("Game type not found: " + gameType));
        }
        String methodName = "productlist";
        String requestTime = LocalDateTime.now().format(formatter);
        String sign = SignUtil.createSignatureForRequest(operatorCode, Long.parseLong(requestTime), methodName, secretKey);
        URI uri = UriComponentsBuilder.fromHttpUrl(thirdPartyRoute)
                .queryParam("operator_code", operatorCode)
                .queryParam("request_time", requestTime)
                .queryParam("sign", sign)
                .build()
                .toUri();

        log.info("Request URI: {}" , uri);
        ResponseEntity<List<ProviderDataFeign>> response;
        try {
             response = restTemplate.exchange(uri, HttpMethod.GET,
                     null,
                     new ParameterizedTypeReference<List<ProviderDataFeign>>() {});
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(ProviderResponse.builder()
                            .code(500)
                            .message(e.getMessage())
                    .build());
        }
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            List<ProviderDataFeign> providerListFeignResponse = response.getBody();
            List<ProviderResponse.ProviderData> providerList;
             if (gameType==null){
                 providerList = providerListFeignResponse.stream().map(ProviderResponse.ProviderData::of).toList();
             }else {
                 // Filter providers by game type
                 providerList = providerListFeignResponse.stream()
                         .filter(provider -> provider.getGameType().equalsIgnoreCase(gameType))
                         .map(ProviderResponse.ProviderData::of)
                         .toList();
             }

            return ResponseEntity.ok(ProviderResponse.builder()
                    .code(200)
                    .message("Success")
                    .providers(providerList)
                    .build());
        }
        return null;
    }

    @Override
    @Transactional
    public ResponseEntity<String> syncProviders() {
        List<GameType> gameTypeList = gameTypeRepo.findAll();
        if (gameTypeList.isEmpty()) {
            return ResponseEntity.ok("No game types found.");
        }

        for (GameType gameType : gameTypeList) {
            if (blackListGameTypeCodes.contains(gameType.getCode())) {
                continue;
            }

            ResponseEntity<ProviderResponse> response = getProviderListByGameType(gameType.getCode());
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                List<ProviderResponse.ProviderData> providerListFromGameSoft = response.getBody().getProviders();

                List<Long> productListFromDatabase = gameProviderRepo.findByGameType(gameType)
                        .stream()
                        .map(GameSoftGameProvider::getProduct)
                        .toList();

                log.info("providerListFromDatabase : {}", productListFromDatabase);
                log.info("providerListFromGameSoft : {}",
                        providerListFromGameSoft.stream()
                                .map(ProviderResponse.ProviderData::getProductCode)
                                .toList()
                );

                List<ProviderResponse.ProviderData> newProviderList = providerListFromGameSoft.stream()
                        .filter(providerData -> !productListFromDatabase.contains(providerData.getProductCode()))
                        .toList();

                log.info("newProviderList : {}",
                        newProviderList.stream()
                                .map(ProviderResponse.ProviderData::getProductCode)
                                .toList()
                );

                for (ProviderResponse.ProviderData newProvider : newProviderList) {
                    GameSoftGameProvider gameProvider = GameSoftGameProvider.of(newProvider, gameType);
                    gameProviderRepo.save(gameProvider);
                }
            }
        }

        return ResponseEntity.ok("Sync completed.");
    }

}
