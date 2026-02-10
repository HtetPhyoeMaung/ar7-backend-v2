package com.security.spring.thirdpartygames.lunchGame.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.security.spring.exceptionall.DataNotFoundException;
import com.security.spring.gamebank.model.GameBankSetting;
import com.security.spring.gamebank.repo.GameBankSettingRepo;
import com.security.spring.thirdpartygames.callback.dto.GameBankResponse;
import com.security.spring.thirdpartygames.callback.dto.LaunchGameBankDto;
import com.security.spring.thirdpartygames.gameType.entity.GameType;
import com.security.spring.thirdpartygames.gameType.repo.GameTypeRepo;
import com.security.spring.thirdpartygames.gameprovider.entity.GameSoftGameProvider;
import com.security.spring.thirdpartygames.gameprovider.repository.GameProviderRepo;
import com.security.spring.thirdpartygames.lunchGame.dto.LunchGameRequest;
import com.security.spring.thirdpartygames.lunchGame.dto.LunchGameResponse;
import com.security.spring.user.entity.User;
import com.security.spring.user.repository.UserRepository;
import com.security.spring.utils.ConstantInformationForGameSoft;
import com.security.spring.utils.SignUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class LunchGameServiceImpl implements LunchGameService {

    private final UserRepository userRepository;
    private final RestTemplate restTemplate;
    private final GameTypeRepo gameTypeRepo;
    private final GameProviderRepo gameProviderRepo;
    private final GameBankSettingRepo gameBankSettingRepo;
    private final ConstantInformationForGameSoft constantDataObj;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    @Override
    @Transactional
    public LunchGameResponse lunchGameService(LunchGameRequest data, String ar7Id) {

        User userObj = userRepository.findByAr7Id(ar7Id).orElseThrow(() -> new RuntimeException("No User Found"));
        String memberName = userObj.getAr7Id();
        String displayName = userObj.getName();
        String password = userObj.getPassword();

        String requestTime = LocalDateTime.now().format(formatter);
        String operatorCode = constantDataObj.getOperatorCode();
        String secretKey = constantDataObj.getSecretKey();
        String methodName = "launchgame";
        String sign = SignUtil.createSignatureForRequest(operatorCode, Long.parseLong(requestTime), methodName, secretKey);



            GameType gameType = gameTypeRepo.findByCodeIgnoreCase(data.getGameType())
                    .orElseThrow(() -> new DataNotFoundException("No Game Type Found"));
            GameSoftGameProvider provider = gameProviderRepo.findByProductAndGameType(Long.valueOf(data.getProductID()), gameType)
                    .orElseThrow(() -> new DataNotFoundException("No Game Provider Found"));


        System.out.println("Lunch Game" + data.toString());

        // Create Request Object
        LunchGameRequest requestObj = LunchGameRequest
                .builder()
                .operatorCode(operatorCode)
                .memberName(memberName)
                .displayName(displayName)
                .password(password)
                .productID(data.getProductID())
                .gameType(data.getGameType())
                .languageCode(String.valueOf(constantDataObj.getLanguageCode()))
                .operatorLobbyURL("https://ar7.org/home")
                .gameID(data.getGameID())
                .platform(data.getPlatform())
                .currency(provider.getCurrencyCode())
                .iPAddress("127.0.0.1")
                .sign(sign)
                .requestTime(requestTime)
                .build();

        // Header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create Request Entity
        HttpEntity<LunchGameRequest> requestEntity = new HttpEntity<>(requestObj, headers);

        System.out.println("Lunch Game Request " + requestObj);
        ResponseEntity<LunchGameResponse> response;
        if (data.getProductID() == 2026) {
            GameBankSetting gameBankSetting = gameBankSettingRepo.findAll().stream()
                    .filter(setting -> setting.getId() == 1).findFirst()
                    .orElseThrow(() -> new DataNotFoundException("Default Game Bank Setting Not Found!"));
            String gameBankRoute = gameBankSetting.getCallBackUrl() + "/api/buffalo/v1/system/access";
            LaunchGameBankDto launchGameBankDto = LaunchGameBankDto.of(gameBankSetting.getAgentId(), gameBankSetting.getAgentCode(), data.getGameID(), memberName, displayName, userObj.getUserUnits().getMainUnit());
            HttpEntity<LaunchGameBankDto> gameBankRequestBody = new HttpEntity<>(launchGameBankDto, headers);
            ResponseEntity<GameBankResponse> gameBankResponse = restTemplate.exchange(gameBankRoute, HttpMethod.POST, gameBankRequestBody,
                    new ParameterizedTypeReference<GameBankResponse>() {
                    });
            assert gameBankResponse.getBody() != null;
            response = LunchGameResponse.fromGameBank(gameBankResponse.getBody());
        } else {
            // Send Request With Post
            String apiUrl = constantDataObj.getApiUrl();
            String thirdPartyRoute = apiUrl + "/api/operators/launch-game";
            response = restTemplate.exchange(thirdPartyRoute, HttpMethod.POST, requestEntity,
                    new ParameterizedTypeReference<LunchGameResponse>() {
                    });
        }
        try {
            System.out.println(new ObjectMapper().writeValueAsString(response.getBody()));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return response.getBody();
    }
}
