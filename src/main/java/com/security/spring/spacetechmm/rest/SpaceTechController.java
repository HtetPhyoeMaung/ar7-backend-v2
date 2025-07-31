package com.security.spring.spacetechmm.rest;

import com.security.spring.config.JWTService;
import com.security.spring.exceptionall.DataNotFoundException;
import com.security.spring.gamesoft.gameType.repo.GameTypeRepo;
import com.security.spring.spacetechmm.dto.SpaceTechResponse;
import com.security.spring.spacetechmm.dto.SpaceTechGameRegisterObj;
import com.security.spring.spacetechmm.entity.GameTable;
import com.security.spring.spacetechmm.entity.SpaceTechGame;
import com.security.spring.spacetechmm.repo.GameTableRepository;
import com.security.spring.spacetechmm.repo.SpaceTechRepository;
import com.security.spring.unit.entity.UserUnits;
import com.security.spring.unit.repo.UnitRepository;
import com.security.spring.user.entity.User;
import com.security.spring.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RestController
@RequestMapping("/api/v1/spacetech-game")
@RequiredArgsConstructor
public class SpaceTechController {

    private final SpaceTechRepository spaceTechRepository;
    private final JWTService jwtService;
    private final UserRepository userRepository;
    private final GameTableRepository gameTableRepository;
    private final UnitRepository unitRepository;

    private final RestTemplate restTemplate;
    private static final String URL = "https://developer.casino909.com/register.php";
    private static final String domain = "ar7myanmar.com";

    @PostMapping("/launch")
    public ResponseEntity<SpaceTechResponse> register(@RequestHeader("Authorization") String  token,
                                                      @RequestParam(name = "spaceTechGameId") Long spaceTechGameId,
                                                      @RequestParam(name = "tableId", required = false) Long tableId){


        String ar7Id = jwtService.extractUsername(token.substring(7));
        log.info("Reach State ONE");
        User user = userRepository.findByAr7Id(ar7Id).orElseThrow(()->
                new DataNotFoundException("User not found by Ar7ID : "+ar7Id));
        UserUnits userUnits = user.getUserUnits();
        log.info("Reach State TWO");
       
        SpaceTechGame spaceTechGame = spaceTechRepository.findById(spaceTechGameId).orElseThrow(()->
                new DataNotFoundException("SpaceTechGame not found by ID : "+spaceTechGameId));
        GameTable gameTable = new GameTable();
        if (tableId!=null){
             gameTable = gameTableRepository.findById(tableId).orElseThrow(()->
                    new DataNotFoundException("Game Table not found by ID : "+tableId));

            int minAmount = gameTable.getMiniBet();
            int maxAmount = gameTable.getMaxBet();

            if (userUnits.getMainUnit()<minAmount || userUnits.getMainUnit()>maxAmount){
                throw new UnsupportedOperationException("တောင်းပန်ပါတယ် သင်ဧ။် လက်ရှိငွေပမာသည် ယခုဝိုင်းအားဝင်ခွင့်မရှိပါ။ ကျေးဇူးပြုပြီး သင့်တော်သော် ဝိုင်းအားရွေးချယ်ပေးပါရန် တောင်းဆိုအပ်ပါသည်။");
            }
        }

        
        log.info("Reach State THREE");
        SpaceTechGameRegisterObj spaceTechGameRegisterObj = SpaceTechGameRegisterObj.builder()
                .game(spaceTechGame.getGameName())
                .domain(domain)
                .build();
        log.info("Reach State FOUR");
        if (spaceTechGame.getGameType().getCode().equals(18)){

            spaceTechGameRegisterObj.setId(ar7Id);
            spaceTechGameRegisterObj.setLevel(gameTable.getLevel());
            spaceTechGameRegisterObj.setBalance((int) userUnits.getMainUnit());
            spaceTechGameRegisterObj.setNickname(user.getName());
            spaceTechGameRegisterObj.setProfile(String.valueOf(Math.round(Math.random() * 11 +1)));
        }else {
            spaceTechGameRegisterObj.setId(ar7Id);
            spaceTechGameRegisterObj.setBalance((int) userUnits.getMainUnit());
        }
        log.info("Reach State FIVE");
        SpaceTechResponse response = restTemplate.postForObject(URL, spaceTechGameRegisterObj, SpaceTechResponse.class);
        userUnits.setMainUnit(0);
        unitRepository.save(userUnits);
        return ResponseEntity.ok(response);
    }





}
