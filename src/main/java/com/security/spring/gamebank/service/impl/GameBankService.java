package com.security.spring.gamebank.service.impl;

import com.security.spring.gamebank.dto.GameBankBalanceResponse;
import com.security.spring.gamebank.dto.GameBankResultResponse;
import com.security.spring.gamebank.model.GameBankSetting;
import com.security.spring.gamebank.param.GameBankBalanceRequest;
import com.security.spring.gamebank.param.GameBankBuyScatterRequest;
import com.security.spring.gamebank.param.GameBankResultRequest;
import com.security.spring.gamebank.repo.GameBankSettingRepo;
import com.security.spring.gamebank.service.IGameBankService;
import com.security.spring.exceptionall.DataNotFoundException;
import com.security.spring.exceptionall.ApiDuplicateTransaction;
import com.security.spring.exceptionall.UnauthorizedException;
import com.security.spring.exceptionall.InsufficientBalanceException;
import com.security.spring.thirdpartygames.gameType.entity.GameType;
import com.security.spring.thirdpartygames.gameType.repo.GameTypeRepo;
import com.security.spring.thirdpartygames.transaction.entity.GameSoftTransaction;
import com.security.spring.thirdpartygames.transaction.repsitory.GameSoftTransactionRepo;
import com.security.spring.user.entity.User;
import com.security.spring.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class GameBankService implements IGameBankService {

    private final UserRepository userRepository;
    private final GameBankSettingRepo gameBankSettingRepo;
    private final GameSoftTransactionRepo gameSoftTransactionRepo;
    private final GameTypeRepo gameTypeRepo;


    @Override
    @Transactional
    public ResponseEntity<GameBankBalanceResponse> getBalance(GameBankBalanceRequest gameBankBalanceRequest) {

        log.info("Received Get Balance Call Back Success.");

        // retrieve User by playerId
        User user = userRepository.findByAr7Id(gameBankBalanceRequest.getPlayerId())
                .orElseThrow(()->
                        new DataNotFoundException("User Not Found By Player Id : "
                                + gameBankBalanceRequest.getPlayerId()));

        GameBankSetting gameBankSetting = gameBankSettingRepo.findAll().stream()
                .filter(setting -> setting.getId()==1).findFirst()
                .orElseThrow(()-> new DataNotFoundException("Default Game Bank Setting Not Found!"));

        if (!gameBankBalanceRequest.getAgentCode().equals(gameBankSetting.getAgentCode()) ||
        !gameBankBalanceRequest.getAgentId().equals(gameBankSetting.getAgentId())){
            throw new UnauthorizedException("You're Unauthorized!");
        }

        double balance = user.getUserUnits().getMainUnit();

        var responseDto = GameBankBalanceResponse.
                of(user.getAr7Id(),gameBankSetting.getAgentCode(),balance);

        return ResponseEntity.ok(responseDto);
    }

    @Override
    @Transactional
    public ResponseEntity<GameBankResultResponse> setResult(GameBankResultRequest gameBankResultRequest) {
        log.info("Game Bank Request : {}",gameBankResultRequest);

        GameBankSetting gameBankSetting = gameBankSettingRepo.findAll().stream()
                .filter(setting -> setting.getId()==1).findFirst()
                .orElseThrow(() -> new DataNotFoundException("Default Game Bank Setting Not Found!"));

        if (!gameBankSetting.getAgentCode().equals(gameBankResultRequest.getAgentCode())
        || !gameBankSetting.getAgentId().equals(gameBankResultRequest.getAgentId())){
            throw new UnauthorizedException("You're Unauthorized!");
        }

        User user = userRepository.findByAr7Id(gameBankResultRequest.getPlayerId().toString())
                .orElseThrow(() -> new DataNotFoundException("User Not Found By Player Id : "
                        + gameBankResultRequest.getPlayerId()));

        gameSoftTransactionRepo.findBySpinId(gameBankResultRequest.getSpinId())
                .ifPresent(tx -> {
                    throw new ApiDuplicateTransaction("Duplicate Spin Id");
                });

        double beforeBalance = user.getUserUnits().getMainUnit();
        double afterBalance = (beforeBalance - gameBankResultRequest.getBetAmount()) + gameBankResultRequest.getPayout();
        user.getUserUnits().setMainUnit(afterBalance);

        GameType gameType = gameTypeRepo.findByCode("SLOT").orElseThrow(()->
                new DataNotFoundException("Game Type not found by Code : "+"SLOT"));

        GameSoftTransaction transaction = GameSoftTransaction.builder()
                .spinId(gameBankResultRequest.getSpinId())
                .betAmount(gameBankResultRequest.getBetAmount())
                .amount(gameBankResultRequest.getPayout())
                .beforeBalance(beforeBalance)
                .afterBalance(afterBalance)
                .gameType(gameType)
                .gameCode(gameBankResultRequest.getGameName())
                .gameSoftTransitionUser(user)
                .status("SETTLED")
                .createdOn(LocalDateTime.now())
                .build();

        gameSoftTransactionRepo.save(transaction);
        userRepository.save(user);

        var responseDto = GameBankResultResponse.builder()
                .message("success")
                .status(true)
                .build();

        return ResponseEntity.ok(responseDto);
    }

    @Override
    @Transactional
    public ResponseEntity<GameBankResultResponse> buyScatter(GameBankBuyScatterRequest gameBankBuyScatterRequest) {
        log.info("Buy Scatter Call Back Request : {}", gameBankBuyScatterRequest);

        GameBankSetting gameBankSetting = gameBankSettingRepo.findAll().stream()
                .filter(setting -> setting.getId()==1).findFirst()
                .orElseThrow(() -> new DataNotFoundException("Default Game Bank Setting Not Found!"));

        if (!gameBankSetting.getAgentCode().equals(gameBankBuyScatterRequest.getAgentCode())
                || !gameBankSetting.getAgentId().equals(gameBankBuyScatterRequest.getAgentId())){
            throw new UnauthorizedException("You're Unauthorized!");
        }

        User user = userRepository.findByAr7Id(gameBankBuyScatterRequest.getPlayerId())
                .orElseThrow(() -> new DataNotFoundException("User Not Found By Player Id : "
                        + gameBankBuyScatterRequest.getPlayerId()));

        double deduction = gameBankBuyScatterRequest.getUserDeductBalance();
        if (gameBankBuyScatterRequest.getLegacyUserBalance() != null) {
            deduction = gameBankBuyScatterRequest.getLegacyUserBalance();
        }

        double beforeBalance = user.getUserUnits().getMainUnit();

        if (deduction > beforeBalance){
            throw new InsufficientBalanceException("Insufficient Balance", user.getAr7Id());
        }
        // deduct the requested amount from current balance
        double afterBalance = beforeBalance - deduction;
        user.getUserUnits().setMainUnit(afterBalance);
        userRepository.save(user);

        GameType gameType = gameTypeRepo.findByCode("SLOT").orElseThrow(()->
                new DataNotFoundException("Game Type not found by Code : "+"SLOT"));

        var responseDto = GameBankResultResponse.builder()
                .message("success")
                .status(true)
                .build();

        return ResponseEntity.ok(responseDto);
    }
}
