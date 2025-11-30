package com.security.spring.buffalo.controller;

import com.security.spring.buffalo.dto.BuffaloBalanceDto;
import com.security.spring.buffalo.param.BuffaloBalanceParam;
import com.security.spring.buffalo.service.IBuffaloService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/buffalo/seamless")
@RequiredArgsConstructor
public class CallBackController {

    private final IBuffaloService buffaloService;

    @PostMapping("/balance")
    public ResponseEntity<BuffaloBalanceDto> getBalance(
            @RequestBody BuffaloBalanceParam buffaloBalanceParam
    ){
        return buffaloService.getBalance(buffaloBalanceParam);
    }
}
