package com.security.spring.deposit.dto;

import com.security.spring.deposit.entity.DepositStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class DepositObj {
    private Long id;
    private double amount;
    private String fromUsername;
    private String fromBankName;
    private String toUserName;
    private String bankAcc;
    private String userTransitionId;
    private String adminTransitionId;
    private DepositStatus status;
    private LocalDateTime actionTime;
}
