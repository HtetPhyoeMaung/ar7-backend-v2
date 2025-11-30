package com.security.spring.buffalo.service;

import com.security.spring.buffalo.dto.BuffaloBalanceDto;
import com.security.spring.buffalo.param.BuffaloBalanceParam;
import org.springframework.http.ResponseEntity;

public interface IBuffaloService {
    ResponseEntity<BuffaloBalanceDto> getBalance(BuffaloBalanceParam buffaloBalanceParam);
}
