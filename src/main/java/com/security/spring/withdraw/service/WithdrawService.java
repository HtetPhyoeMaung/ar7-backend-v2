package com.security.spring.withdraw.service;

import com.security.spring.deposit.entity.DepositStatus;
import com.security.spring.withdraw.dto.WithdrawRequest;
import com.security.spring.withdraw.dto.WithdrawResponse;
import com.security.spring.withdraw.dto.WithdrawStatusRequest;

import java.time.LocalDateTime;

public interface WithdrawService {
    WithdrawResponse saveWithdraw(WithdrawRequest data,String ar7Id);
    WithdrawResponse getWithdrawOwn(String ar7Id, int page, int size, LocalDateTime startDate, LocalDateTime endDate, DepositStatus withdrawStatus);
    WithdrawResponse getWithdrawTo(String token, int page, int size, LocalDateTime startDate, LocalDateTime endDate);
    WithdrawResponse getWithdrawFrom(String ar7Id, int page, int size);
    WithdrawResponse getWithdrawById(Long widthDrawId,String ar7Id);
    WithdrawResponse updateWithdraw(WithdrawStatusRequest data, String ar7Id);

    WithdrawResponse getDepositAll(String token, int page, int size, LocalDateTime startDate, LocalDateTime endDate);
}

