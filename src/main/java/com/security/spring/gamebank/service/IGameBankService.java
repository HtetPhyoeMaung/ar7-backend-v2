package com.security.spring.gamebank.service;

import com.security.spring.gamebank.dto.GameBankBalanceResponse;
import com.security.spring.gamebank.dto.GameBankResultResponse;
import com.security.spring.gamebank.param.GameBankBalanceRequest;
import com.security.spring.gamebank.param.GameBankResultRequest;
import org.springframework.http.ResponseEntity;

public interface IGameBankService {
    ResponseEntity<GameBankBalanceResponse> getBalance(GameBankBalanceRequest gameBankBalanceRequest);

    ResponseEntity<GameBankResultResponse> setResult(GameBankResultRequest gameBankResultRequest);
}
