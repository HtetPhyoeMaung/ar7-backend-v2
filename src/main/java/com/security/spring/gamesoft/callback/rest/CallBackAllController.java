package com.security.spring.gamesoft.callback.rest;

import com.security.spring.gamesoft.callback.dto.*;
import com.security.spring.gamesoft.callback.service.CallBackDepositService;
import com.security.spring.gamesoft.callback.service.GetBalanceService;
import com.security.spring.gamesoft.callback.service.PlaceBetService;
import com.security.spring.gamesoft.callback.service.PushBetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/v1/api/seamless")
@RequiredArgsConstructor
public class CallBackAllController {

    private final  GetBalanceService getBalanceServiceImpl;
    private final  PlaceBetService placeBetServiceImpl;
    private final CallBackDepositService callBackDepositService;
    private final PushBetService pushBetServiceImpl;

    @PostMapping("/balance")
    public ResponseEntity<Response<GetBalanceCallBackResponse>> getbalance(@RequestBody CallBackRequest data){
        var responseObj = getBalanceServiceImpl.getBalanceService(data);
        return ResponseEntity.ok(responseObj);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<Response<TransactionCallBackResponse>> withdraw (@RequestBody CallBackRequest data){
        var resObj = placeBetServiceImpl.placebetConfig(data);
        return ResponseEntity.ok(resObj);
    }

    @PostMapping("/deposit")
    public ResponseEntity<Response<TransactionCallBackResponse>> deposit(@RequestBody CallBackRequest data){
        var response = callBackDepositService.depositConfig(data);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/pushbetdata")
    public ResponseEntity<PushBetResponse> putBet(@RequestBody PushBetRequest data){
        var resObj = pushBetServiceImpl.pushbetConfig(data);
        return ResponseEntity.ok(resObj);
    }
}
