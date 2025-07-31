package com.security.spring.commission.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserWinLoseDetailObj {
    private Integer id;
    private String ar7Id;
    private double totalBetAmount;
    private double totalWinAmount;
    private int transactionLines;
    private String gameCode;
    private String gameTypeName;
    private String  gameTypeCode;
    private String parentAgentId;
    private String masterId;
    private String seMasterId;
    private double masterCommissionPercentage;
    private LocalDateTime calculateDate;
    private double seCommissionPercentage;
    private double agentCommissionPercentage;
    private double agentWinLose;

}
