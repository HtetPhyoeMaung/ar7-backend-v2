package com.security.spring.gamesoft.getGameList.service;

import com.security.spring.exceptionall.ApiMemberDoesNotExist;
import com.security.spring.exceptionall.DataNotFoundException;
import com.security.spring.gamesoft.gameType.entity.GameType;
import com.security.spring.gamesoft.gameType.repo.GameTypeRepo;
import com.security.spring.gamesoft.gameprovider.entity.GameSoftGameProvider;
import com.security.spring.gamesoft.gameprovider.repository.GameProviderRepo;
import com.security.spring.gamesoft.getGameList.dto.GameListResponse;
import com.security.spring.gamesoft.getGameList.dto.GetGameListRequest;
import com.security.spring.gamesoft.getGameList.dto.GetGameListResponse;
import com.security.spring.spacetechmm.service.SpaceTechService;
import com.security.spring.user.entity.User;
import com.security.spring.user.repository.UserRepository;
import com.security.spring.utils.ConstantInformationForGameSoft;
import com.security.spring.utils.ErrorMessageUtil;
import com.security.spring.utils.SignUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetGameListServiceImpl implements GetGameListService {

    private final RestTemplate restTemplate;

    private final UserRepository userRepository;

    private final SpaceTechService spaceTechService;

    private final GameTypeRepo gameTypeRepo;

    private final GameProviderRepo gameProviderRepo;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    ConstantInformationForGameSoft constantDataObj = ConstantInformationForGameSoft.builder().build();

    private final String operatorCode = constantDataObj.getOperatorCode();
    private final String secretKey = constantDataObj.getSecretKey();
    private int productID;
    private int gameType;
    private final String  languageCode = String.valueOf(constantDataObj.getLanguageCode());
    private final String apiUrl = constantDataObj.getApiUrl();
    private final String thirdPartyRoute = apiUrl + "/api/operators/provider-games";

    @Override
    @Transactional
    public GetGameListResponse getGameListConfig(GetGameListRequest data, String ar7id) {
        User currentUser = userRepository.findByAr7Id(ar7id).orElseThrow(() -> new ApiMemberDoesNotExist(ErrorMessageUtil.API_MEMBER_NOT_EXISTS));
        String memberName = currentUser.getAr7Id();
        String displayName = currentUser.getName();
        String methodName = "gamelist";
        String requestTime = LocalDateTime.now().format(formatter);
        String sign = SignUtil.createSignatureForRequest(operatorCode, Long.parseLong(requestTime), methodName, secretKey);

        URI uri = UriComponentsBuilder.fromHttpUrl(thirdPartyRoute)
                .queryParam("operator_code", operatorCode)
                .queryParam("product_code", data.getProductID())
                .queryParam("game_type", data.getGameType())
                .queryParam("request_time", requestTime)
                .queryParam("sign", sign)
                .build()
                .toUri();

        log.info("Get Game List Request URI: {}", uri);

        // Send GET request
        ResponseEntity<GameListResponse> response = restTemplate.getForEntity(uri, GameListResponse.class);

        // Get provider info
        GameType gameTypeObj = gameTypeRepo.findByCode(data.getGameType()).orElseThrow(() ->
                new DataNotFoundException("GameType not found by Description : " + data.getGameType()));
        GameSoftGameProvider gameSoftGameProvider = gameProviderRepo.findByProductAndGameType(
                (long) data.getProductID(), gameTypeObj).orElseThrow(() ->
                new DataNotFoundException("GameProvider not found by Product and GameType."));

        log.info("Get Game List Response: {}", response.getBody());
        return GetGameListResponse.builder()
                .gameListResponse(response.getBody())
                .productName(gameSoftGameProvider.getProductCode())
                .build();

    }


}