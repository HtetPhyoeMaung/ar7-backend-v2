
package com.security.spring.utils;

import com.security.spring.gamesoft.callback.dto.CallBackTransaction;
import com.security.spring.gamesoft.gameType.entity.GameType;
import com.security.spring.gamesoft.gameprovider.entity.GameSoftGameProvider;
import com.security.spring.gamesoft.wager.entity.GameSoftWager;
import com.security.spring.gamesoft.wager.repository.GameSoftWagerReop;
import com.security.spring.gamesoft.wager.service.GameSoftWagerService;
import com.security.spring.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


@Component
@RequiredArgsConstructor
@Slf4j
public class CommonUtil {
    private final GameSoftWagerService gameSoftWagerService;
    private final GameSoftWagerReop gameSoftWagerReop;

    public GameSoftWager updateOrSaveWager(CallBackTransaction requestObj, GameType gameType, GameSoftGameProvider gameProvider, User updatedUser) {
        var gameSoftWager =gameSoftWagerService.findByWagerCode(requestObj.getWagerCode());

        if (gameSoftWager.isEmpty()){
            return saveNewWager(requestObj,gameType,gameProvider,updatedUser);

        }else {
            GameSoftWager updateWager = gameSoftWager.get();
            updateWager.setWagerCode(requestObj.getWagerCode());
            updateWager.setGameSoftWagerUser(updatedUser);
            updateWager.setCurrencyID(requestObj.getCurrency());
            updateWager.setGameCode(requestObj.getGameCode());
            updateWager.setGameRoundID(requestObj.getRoundId());
            updateWager.setValidBetAmount(requestObj.getValidBetAmount());
            updateWager.setBetAmount(requestObj.getBetAmount());
            updateWager.setPrizeAmount(requestObj.getPrizeAmount());
            updateWager.setSettlementDate(requestObj.getSettleAt());
            updateWager.setStatus(requestObj.getWagerStatus());
            updateWager.setModifiedOn(LocalDateTime.now());

            var saveUpdateWager = gameSoftWagerReop.save(updateWager);
            log.info("Save Update Wager {} : ",saveUpdateWager);
            return saveUpdateWager;
        }
    }


    private GameSoftWager saveNewWager(CallBackTransaction requestObj, GameType gameType, GameSoftGameProvider gameProvider, User updatedUser) {
        GameSoftWager wager = MapperUtil.mapToWager(requestObj,gameType,gameProvider,updatedUser);

        var saveWager = gameSoftWagerReop.save(wager);
        log.info("Save New Wager {} : ",saveWager);
        return saveWager;
    }





}
