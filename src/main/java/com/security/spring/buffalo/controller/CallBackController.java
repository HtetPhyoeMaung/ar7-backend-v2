package com.security.spring.buffalo.controller;

import com.security.spring.buffalo.dto.GameBankBalanceResponse;
import com.security.spring.buffalo.dto.GameBankResultResponse;
import com.security.spring.buffalo.param.GameBankBalanceRequest;
import com.security.spring.buffalo.param.GameBankResultRequest;
import com.security.spring.buffalo.service.IGameBankService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/game-bank/seamless")
@RequiredArgsConstructor
public class CallBackController {

    private final IGameBankService gameBankService;

    @PostMapping("/balance")
    public ResponseEntity<GameBankBalanceResponse> getBalance(
            @RequestBody GameBankBalanceRequest gameBankBalanceRequest
    ){
        return gameBankService.getBalance(gameBankBalanceRequest);
    }

    @PostMapping("/result")
    public ResponseEntity<GameBankResultResponse> setResult(
            @RequestBody GameBankResultRequest gameBankResultRequest
            ){
        return gameBankService.setResult(gameBankResultRequest);
    }

}
