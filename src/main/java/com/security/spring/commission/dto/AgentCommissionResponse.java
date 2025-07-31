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
public class AgentCommissionResponse {
    private String agentAr7Id;
    private String masterAr7Id;
    private String seMasterAr7Id;
    private double totalUser;
    private double totalBetAmount;
    private double totalWinAmount;
    private double agentWinLoseAmount;
    private double masterWinLoseAmount;
    private double seMasterWinLoseAmount;
    private LocalDateTime confirmDate;
}
