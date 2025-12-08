package com.security.spring.buffalo.service.impl;

import com.security.spring.buffalo.dto.GameBankBalanceResponse;
import com.security.spring.buffalo.dto.GameBankResultResponse;
import com.security.spring.buffalo.model.BuffaloSetting;
import com.security.spring.buffalo.param.GameBankBalanceRequest;
import com.security.spring.buffalo.param.GameBankResultRequest;
import com.security.spring.buffalo.repo.BuffaloSettingRepo;
import com.security.spring.buffalo.service.IGameBankService;
import com.security.spring.exceptionall.DataNotFoundException;
import com.security.spring.exceptionall.UnauthorizedException;
import com.security.spring.user.entity.User;
import com.security.spring.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GameBankService implements IGameBankService {

    private final UserRepository userRepository;
    private final BuffaloSettingRepo buffaloSettingRepo;


    @Override
    @Transactional
    public ResponseEntity<GameBankBalanceResponse> getBalance(GameBankBalanceRequest gameBankBalanceRequest) {

        log.info("Received Get Balance Call Back Success.");

        // retrieve User by playerId
        User user = userRepository.findByAr7Id(gameBankBalanceRequest.getPlayerId())
                .orElseThrow(()->
                        new DataNotFoundException("User Not Found By Player Id : "
                                + gameBankBalanceRequest.getPlayerId()));

        BuffaloSetting buffaloSetting = buffaloSettingRepo.findAll().stream()
                .filter(setting -> setting.getId()==1).findFirst()
                .orElseThrow(()-> new DataNotFoundException("Default Buffalo Setting Not Found!"));

        if (!gameBankBalanceRequest.getAgentCode().equals(buffaloSetting.getAgentCode()) ||
        !gameBankBalanceRequest.getPassword().equals(buffaloSetting.getPassword())){
            throw new UnauthorizedException("You're Unauthorized!");
        }

        double balance = user.getUserUnits().getMainUnit();

        var responseDto = GameBankBalanceResponse.
                of(user.getAr7Id(),buffaloSetting.getAgentCode(),balance);

        return ResponseEntity.ok(responseDto);
    }

    @Override
    @Transactional
    public ResponseEntity<GameBankResultResponse> setResult(GameBankResultRequest gameBankResultRequest) {
    log.info("Game Bank Request : {}",gameBankResultRequest);
    return null;
    }
}
