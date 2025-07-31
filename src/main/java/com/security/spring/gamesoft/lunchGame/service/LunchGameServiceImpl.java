package com.security.spring.gamesoft.lunchGame.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.security.spring.exceptionall.DataNotFoundException;
import com.security.spring.gamesoft.gameType.entity.GameType;
import com.security.spring.gamesoft.gameType.repo.GameTypeRepo;
import com.security.spring.gamesoft.gameprovider.entity.GameSoftGameProvider;
import com.security.spring.gamesoft.gameprovider.repository.GameProviderRepo;
import com.security.spring.gamesoft.lunchGame.dto.LunchGameRequest;
import com.security.spring.gamesoft.lunchGame.dto.LunchGameResponse;
import com.security.spring.gamesoft.transaction.entity.MemberSign;
import com.security.spring.gamesoft.transaction.repsitory.MemberSignRepo;
import com.security.spring.gamesoft.transaction.service.MemberSignService;
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
public class LunchGameServiceImpl implements LunchGameService{


    private final UserRepository userRepository;

    private final MemberSignRepo memberSignRepo;

    private final RestTemplate restTemplate;

    private final GameTypeRepo gameTypeRepo;

    private final GameProviderRepo gameProviderRepo;

    private final MemberSignService memberSignService;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    ConstantInformationForGameSoft constantDataObj = ConstantInformationForGameSoft.builder().build();
    private String operatorCode = constantDataObj.getOperatorCode();
    private String memberName;
    private String displayName;
    private String password;
    private String  languageCode = String.valueOf(constantDataObj.getLanguageCode());


    private String apiUrl = constantDataObj.getApiUrl();
    private String thirdPartyRoute = apiUrl+"/api/operators/launch-game";
    private String sign;
    private String requestTime;
    private String methodName = "launchgame";
    private String secretKey = constantDataObj.getSecretKey();

    @Override
    @Transactional
    public LunchGameResponse lunchGameService(LunchGameRequest data,String ar7Id){

        User userObj = userRepository.findByAr7Id(ar7Id).orElseThrow(() -> new RuntimeException("No User Found"));
        memberName = userObj.getAr7Id();
        displayName = userObj.getName();
        password = userObj.getPassword();

        requestTime = LocalDateTime.now().format(formatter);
        sign = SignUtil.createSignatureForRequest(operatorCode, Long.parseLong(requestTime),methodName,secretKey);

        GameType gameType = gameTypeRepo.findByCode(data.getGameType())
                .orElseThrow(() -> new DataNotFoundException("No Game Type Found"));

        GameSoftGameProvider provider = gameProviderRepo.findByProductAndGameType(Long.valueOf(data.getProductID()), gameType)
                .orElseThrow(() -> new DataNotFoundException("No Game Provider Found"));

        System.out.println("Lunch Game" + data.toString());

//        Create Request Object
        LunchGameRequest requestObj = LunchGameRequest
                .builder()
                .operatorCode(operatorCode)
                .memberName(memberName)
                .displayName(displayName)
                .password(password)
                .productID(data.getProductID())
                .gameType(data.getGameType())
                .languageCode(languageCode)
                .operatorLobbyURL("https://tripleace.online/home")
                .gameID(data.getGameID())
                .platform(data.getPlatform())
                .currency(provider.getCurrencyCode())
                .iPAddress("127.0.0.1")
                .sign(sign)
                .requestTime(requestTime)
                .build();

//      Header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

//      Create Request Entity
        HttpEntity<LunchGameRequest> requestEntity = new HttpEntity<>(requestObj,headers);

        System.out.println("Lunch Game Request " + requestObj);

//        Send Request With Post
        ResponseEntity<LunchGameResponse> response = restTemplate.exchange(thirdPartyRoute, HttpMethod.POST,requestEntity,
                new ParameterizedTypeReference<LunchGameResponse>(){});

        try {
            System.out.println(new ObjectMapper().writeValueAsString(response.getBody()));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return response.getBody();
    }

    private MemberSign createOrUpdateSign(String sign, String memberName) {
        var result =memberSignService.findByMemberName(memberName);
        if (result.isEmpty()){
            MemberSign createSign = MemberSign.builder().sign(sign).memberName(memberName).build();
            return memberSignRepo.save(createSign);
        }
        var resultGet = result.get();
        resultGet.setSign(sign);
        return memberSignRepo.save(resultGet);
    }
}
