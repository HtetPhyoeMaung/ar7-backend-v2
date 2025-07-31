package com.security.spring.gamesoft.getProviderList.service.impl;

import com.security.spring.exceptionall.DataNotFoundException;
import com.security.spring.gamesoft.gameType.repo.GameTypeRepo;
import com.security.spring.gamesoft.gameprovider.repository.GameProviderRepo;
import com.security.spring.gamesoft.getProviderList.dto.ProviderDataFeign;
import com.security.spring.gamesoft.getProviderList.dto.ProviderResponse;
import com.security.spring.gamesoft.getProviderList.service.ProviderListService;
import com.security.spring.utils.ConstantInformationForGameSoft;
import com.security.spring.utils.SignUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
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

    ConstantInformationForGameSoft constantDataObj = ConstantInformationForGameSoft.builder().build();

    private final String operatorCode = constantDataObj.getOperatorCode();
    private final String secretKey = constantDataObj.getSecretKey();
    private final String apiUrl = constantDataObj.getApiUrl();
    private final String thirdPartyRoute = apiUrl + "/api/operators/available-products";

    @Override
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
}
