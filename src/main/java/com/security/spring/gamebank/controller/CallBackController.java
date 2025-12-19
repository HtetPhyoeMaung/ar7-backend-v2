package com.security.spring.gamebank.controller;

import com.security.spring.gamebank.dto.GameBankBalanceResponse;
import com.security.spring.gamebank.dto.GameBankResultResponse;
import com.security.spring.gamebank.param.GameBankBalanceRequest;
import com.security.spring.gamebank.param.GameBankResultRequest;
import com.security.spring.gamebank.service.IGameBankService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/game-bank/seamless")
@RequiredArgsConstructor
public class CallBackController {

    private final IGameBankService gameBankService;

    @PostMapping("/balance")
    public ResponseEntity<GameBankBalanceResponse> getBalance(
            @RequestBody @Valid GameBankBalanceRequest gameBankBalanceRequest
    ){
        log.info("Get Balance Call Back Request : {}",gameBankBalanceRequest);
        return gameBankService.getBalance(gameBankBalanceRequest);
    }

    @PostMapping("/result")
    public ResponseEntity<GameBankResultResponse> setResult(
            @RequestBody @Valid GameBankResultRequest gameBankResultRequest
            ){
        log.info("Result Call Back Request : {}", gameBankResultRequest);
        return gameBankService.setResult(gameBankResultRequest);
    }

}
