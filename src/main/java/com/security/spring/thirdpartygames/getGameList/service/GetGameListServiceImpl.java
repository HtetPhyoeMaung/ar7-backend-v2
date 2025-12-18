package com.security.spring.thirdpartygames.getGameList.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.security.spring.exceptionall.ApiMemberDoesNotExist;
import com.security.spring.exceptionall.DataNotFoundException;
import com.security.spring.gamebank.model.GameBankSetting;
import com.security.spring.gamebank.repo.GameBankSettingRepo;
import com.security.spring.thirdpartygames.callback.dto.GameBankResponse;
import com.security.spring.thirdpartygames.gameType.entity.GameType;
import com.security.spring.thirdpartygames.gameType.repo.GameTypeRepo;
import com.security.spring.thirdpartygames.gameprovider.entity.GameSoftGameProvider;
import com.security.spring.thirdpartygames.gameprovider.repository.GameProviderRepo;
import com.security.spring.thirdpartygames.getGameList.dto.GameListResponse;
import com.security.spring.thirdpartygames.getGameList.dto.GetGameListRequest;
import com.security.spring.thirdpartygames.getGameList.dto.GetGameListResponse;
import com.security.spring.thirdpartygames.getGameList.dto.ProviderGame;
import com.security.spring.spacetechmm.service.SpaceTechService;
import com.security.spring.user.entity.User;
import com.security.spring.user.repository.UserRepository;
import com.security.spring.utils.ConstantInformationForGameSoft;
import com.security.spring.utils.ErrorMessageUtil;
import com.security.spring.utils.SignUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetGameListServiceImpl implements GetGameListService {

    private final RestTemplate restTemplate;

    private final UserRepository userRepository;

    private final SpaceTechService spaceTechService;

    private final GameTypeRepo gameTypeRepo;

    private final GameProviderRepo gameProviderRepo;
    
    private final GameBankSettingRepo gameBankSettingRepo;

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
    public GetGameListResponse getGameListConfig(GetGameListRequest data, String ar7id) throws JsonMappingException, JsonProcessingException {
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
        ResponseEntity<GameListResponse> response;
        ResponseEntity<String> rawResponse;

        if (data.getProductID() == 2026) {
        	GameBankSetting gameBankSetting = gameBankSettingRepo.findAll().stream()
                .filter(setting -> setting.getId()==1).findFirst()
                .orElseThrow(()-> new DataNotFoundException("Default Game Bank Setting Not Found!"));
        	HttpHeaders headers = new HttpHeaders();
        	headers.setContentType(MediaType.APPLICATION_JSON);

        	HttpEntity<GetGameListRequest> requestEntity =
        	        new HttpEntity<>(GetGameListRequest.builder()
        	                .agentId(gameBankSetting.getAgentId())
        	                .agentCode(gameBankSetting.getAgentCode())
        	                .build(), headers);
        	log.info("agent id {} , agentCode {}",gameBankSetting.getAgentId(),gameBankSetting.getAgentCode());

        	rawResponse =
        	        restTemplate.exchange(
        	                gameBankSetting.getCallBackUrl() + "/api/game/v1/games",
        	                HttpMethod.POST,
        	                requestEntity,
        	                String.class
        	        );
        	log.info("RAW GAMEBANK RESPONSE = {}", rawResponse.getBody());

        	
        	ObjectMapper mapper = new ObjectMapper();


        	JsonNode node = mapper.readTree(rawResponse.getBody());
        	JsonNode dataNode = node.get("data");
        	List<ProviderGame> providerGames =
        	        mapper.convertValue(dataNode, new TypeReference<List<ProviderGame>>() {});
			
			response = ResponseEntity.ok(GameListResponse.builder()
					.providerGames(providerGames)
					.build());
        	
        } else {
            // Send GET request
            response= restTemplate.getForEntity(uri, GameListResponse.class);
        }
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