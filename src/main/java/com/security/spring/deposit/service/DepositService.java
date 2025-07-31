package com.security.spring.deposit.service;

import com.security.spring.deposit.dto.DepositRequest;
import com.security.spring.deposit.dto.DepositResponse;
import com.security.spring.deposit.entity.DepositStatus;

import java.time.LocalDateTime;

public interface DepositService {
    DepositResponse saveDeposit(DepositRequest data,String ar7Id);
    DepositResponse getDepositForMe(String ar7Id, int page, int size, LocalDateTime startDate, LocalDateTime endDate, DepositStatus depositStatus);
    DepositResponse getDepositTo(String ar7Id, int page, int size, LocalDateTime startDate, LocalDateTime endDate);
    DepositResponse getDepositFrom(String ar7Id, int page, int size);
    DepositResponse getDepositAll(String token,int page, int size, LocalDateTime startDate, LocalDateTime endDate);
    DepositResponse updateDeposit(String ar7Id,DepositRequest data);
}
