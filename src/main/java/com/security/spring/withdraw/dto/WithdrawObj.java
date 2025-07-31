package com.security.spring.withdraw.dto;

import com.security.spring.deposit.entity.DepositStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WithdrawObj {
    private Integer id;
    private String withdrawUserName;
    private String withdrawAr7Id;
    private String parentUserName;
    private String parentAR7Id;
    private Integer bankNameId;
    private String bankNameName;
    private String withdrawBankAcc;
    private String withdrawBankAccNumber;
    private String userTransitionId;
    private double amount;
    private LocalDateTime actionTime;
    private DepositStatus withdrawStatus;
}
