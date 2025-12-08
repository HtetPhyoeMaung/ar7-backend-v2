package com.security.spring.buffalo.service;

import com.security.spring.buffalo.dto.GameBankBalanceResponse;
import com.security.spring.buffalo.dto.GameBankResultResponse;
import com.security.spring.buffalo.param.GameBankBalanceRequest;
import com.security.spring.buffalo.param.GameBankResultRequest;
import org.springframework.http.ResponseEntity;

public interface IGameBankService {
    ResponseEntity<GameBankBalanceResponse> getBalance(GameBankBalanceRequest gameBankBalanceRequest);

    ResponseEntity<GameBankResultResponse> setResult(GameBankResultRequest gameBankResultRequest);
}
